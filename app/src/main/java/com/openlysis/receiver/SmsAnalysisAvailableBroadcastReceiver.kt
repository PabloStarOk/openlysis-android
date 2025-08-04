package com.openlysis.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.work.SmsAnalysisRefreshWorker
import com.openlysis.data.work.SmsAnalysisStartWorker
import com.openlysis.data.work.constant.SmsAnalysisWorkers
import com.openlysis.notification.Notifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * BroadcastReceiver that handles SMS analysis-related actions.
 * Listens for broadcasts indicating that an SMS analysis or cancellation
 * should be performed, triggered from a notification action.
 */
@AndroidEntryPoint
internal class SmsAnalysisAvailableBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var notifier: Notifier

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        if (intent.action != SMS_ANALYSIS_AVAILABLE_INTENT) {
            return
        }

        val notificationId =
            intent.getIntExtra(
                EXTRA_NOTIFICATION_ID,
                SmsAnalysisWorkers.DEFAULT_INVALID_NOTIFICATION_ID
            )
        val actionTypeString = intent.getStringExtra(EXTRA_SUB_ACTION)
        val messageSender = intent.getStringExtra(EXTRA_MESSAGE_SENDER)
        val messageBody = intent.getStringExtra(EXTRA_MESSAGE_BODY)

        if (notificationId == SmsAnalysisWorkers.DEFAULT_INVALID_NOTIFICATION_ID) {
            throw IllegalStateException("Notification ID was not found.")
        }

        if (actionTypeString == null) {
            throw IllegalStateException("Broadcast was received but request type is invalid.")
        }

        if (messageSender == null) {
            throw IllegalStateException("Broadcast was received but message sender is invalid.")
        }

        if (messageBody == null) {
            throw IllegalStateException("Broadcast was received but message body was null.")
        }

        val subAction = SubAction.valueOf(actionTypeString)
        when (subAction) {
            SubAction.Analyze -> {
                enqueueWorkers(context, notificationId, messageSender, messageBody)
                if (hasInternetConnection(context)) {
                    notifier.notifyMessageAnalysis(
                        messageSender,
                        AnalysisStatus.Queued,
                        Verdict.Unknown,
                        tapIntent = null,
                        notificationId = notificationId
                    )
                } else {
                    notifier.notifySmsAnalysisPendingByInternet(notificationId, messageSender)
                }
            }
            SubAction.Cancel -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }

    private fun enqueueWorkers(
        context: Context,
        notificationId: Int,
        messageSender: String,
        messageBody: String
    ) {
        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
                .build()

        val smsAnalysisStartWorker =
            OneTimeWorkRequestBuilder<SmsAnalysisStartWorker>()
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        SmsAnalysisWorkers.NOTIFICATION_ID_KEY to notificationId,
                        SmsAnalysisStartWorker.MESSAGE_SENDER_KEY to messageSender,
                        SmsAnalysisStartWorker.MESSAGE_BODY_KEY to messageBody
                    )
                ).build()

        val smsAnalysisRefreshWorker =
            OneTimeWorkRequestBuilder<SmsAnalysisRefreshWorker>()
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                    workDataOf(
                        SmsAnalysisWorkers.NOTIFICATION_ID_KEY to notificationId
                    )
                ).build()

        val workManager = WorkManager.Companion.getInstance(context)
        workManager
            .beginWith(smsAnalysisStartWorker)
            .then(smsAnalysisRefreshWorker)
            .enqueue()
    }

    private fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
        val reachInternet =
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        val isValidated =
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

        return reachInternet && isValidated
    }

    /**
     * Contains constants related to the broadcast receiver's intent and extra data.
     */
    companion object {
        /**
         * Intent action indicating that an SMS analysis is available to be analyzed.
         */
        const val SMS_ANALYSIS_AVAILABLE_INTENT = "com.openlysis.Analysis.SMS_ANALYSIS_AVAILABLE"

        /**
         * Extra key for the notification ID associated with the SMS analysis.
         */
        const val EXTRA_NOTIFICATION_ID =
            "com.openlysis.Analysis.SMS_ANALYSIS_AVAILABLE_NOTIFICATION_ID"

        /**
         * Extra key for the sub-action associated with the SMS analysis.
         */
        const val EXTRA_SUB_ACTION = "com.openlysis.Analysis.SMS_ANALYSIS_AVAILABLE_SUB_ACTION"

        /**
         * Extra key for the message sender in the SMS analysis intent.
         */
        const val EXTRA_MESSAGE_SENDER = "com.openlysis.Analysis.MESSAGE_SENDER"

        /**
         * Extra key for the message body in the SMS analysis intent.
         */
        const val EXTRA_MESSAGE_BODY = "com.openlysis.Analysis.MESSAGE_BODY"
    }

    /**
     * Sub-actions performable by the broadcast receiver.
     */
    enum class SubAction {
        /**
         * Indicates that the SMS should be analyzed.
         */
        Analyze,

        /**
         * Indicates that the notification to analyze the SMS should be canceled.
         */
        Cancel
    }
}