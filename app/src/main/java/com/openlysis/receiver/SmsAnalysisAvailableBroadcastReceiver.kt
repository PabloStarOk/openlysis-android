package com.openlysis.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.openlysis.data.work.SmsAnalysisRefreshWorker
import com.openlysis.data.work.SmsAnalysisStartWorker
import com.openlysis.data.work.constant.SmsAnalysisWorkers

/**
 * BroadcastReceiver that handles SMS analysis-related actions.
 * Listens for broadcasts indicating that an SMS analysis or cancellation
 * should be performed, triggered from a notification action.
 */
internal class SmsAnalysisAvailableBroadcastReceiver : BroadcastReceiver() {
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
        val smsPdu = intent.getByteArrayExtra(EXTRA_SMS_PDU)
        val smsFormat = intent.getStringExtra(EXTRA_SMS_FORMAT)

        if (notificationId == SmsAnalysisWorkers.DEFAULT_INVALID_NOTIFICATION_ID) {
            throw IllegalStateException("Notification ID was not found.")
        }

        if (actionTypeString == null) {
            throw IllegalStateException("Broadcast was received but request type is invalid.")
        }

        if (smsFormat == null) {
            throw IllegalStateException("Broadcast was received but SMS format was null.")
        }

        val subAction = SubAction.valueOf(actionTypeString)
        when (subAction) {
            SubAction.Analyze -> {
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
                                SmsAnalysisStartWorker.SMS_MESSAGE_PDU_KEY to smsPdu,
                                SmsAnalysisStartWorker.SMS_MESSAGE_FORMAT_KEY to smsFormat
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
            SubAction.Cancel -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
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
         * Extra key for the SMS PDU.
         */
        const val EXTRA_SMS_PDU = "com.openlysis.Analysis.SMS_ANALYSIS_PDU"

        /**
         * Extra key for the SMS format.
         */
        const val EXTRA_SMS_FORMAT = "com.openlysis.Analysis.SMS_ANALYSIS_FORMAT"
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