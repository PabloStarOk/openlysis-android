package com.openlysis.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.openlysis.data.work.SmsAnalysisWorker
import com.openlysis.notification.constant.SmsAnalysis

/**
 * BroadcastReceiver that handles SMS analysis-related actions.
 * Listens for broadcasts indicating that an SMS analysis or cancellation
 * should be performed, triggered from a notification action.
 */
internal class AnalyzableSmsReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        if (intent.action != SmsAnalysis.SMS_ANALYSIS_AVAILABLE_INTENT) {
            return
        }

        val notificationId = intent.getIntExtra(SmsAnalysis.EXTRA_NOTIFICATION_ID, -1)
        val actionTypeString = intent.getStringExtra(SmsAnalysis.EXTRA_SUB_ACTION)
        val smsPdu = intent.getByteArrayExtra(SmsAnalysis.EXTRA_SMS_PDU)
        val smsFormat = intent.getStringExtra(SmsAnalysis.EXTRA_SMS_FORMAT)

        if (notificationId == -1) {
            Log.e("AnalyzableSmsReceiver", "Broadcast was received but notification ID is invalid.")
            return
        }

        if (actionTypeString == null) {
            Log.e("AnalyzableSmsReceiver", "Broadcast was received but request type is invalid.")
            return
        }

        if (smsFormat == null) {
            Log.e("AnalyzableSmsReceiver", "Broadcast was received but SMS format was null.")
        }

        val subAction = SmsAnalysis.SubAction.valueOf(actionTypeString)
        when (subAction) {
            SmsAnalysis.SubAction.Analyze -> {
                val constraints =
                    Constraints
                        .Builder()
                        .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
                        .build()

                val smsAnalysisWorker =
                    OneTimeWorkRequestBuilder<SmsAnalysisWorker>()
                        .setConstraints(constraints)
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setInputData(
                            workDataOf(
                                SmsAnalysisWorker.NOTIFICATION_ID_KEY to notificationId,
                                SmsAnalysisWorker.SMS_MESSAGE_PDU_KEY to smsPdu,
                                SmsAnalysisWorker.SMS_MESSAGE_FORMAT_KEY to smsFormat
                            )
                        ).build()

                val workManager = WorkManager.getInstance(context)
                workManager.enqueue(smsAnalysisWorker)
            }
            SmsAnalysis.SubAction.Cancel -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }
}