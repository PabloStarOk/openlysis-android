package com.openlysis.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
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

        if (notificationId == -1) {
            Log.e("AnalyzableSmsReceiver", "Broadcast was received but notification ID is invalid.")
            return
        }

        if (actionTypeString == null) {
            Log.e("AnalyzableSmsReceiver", "Broadcast was received but request type is invalid.")
            return
        }

        val subAction = SmsAnalysis.SubAction.valueOf(actionTypeString)
        when (subAction) {
            SmsAnalysis.SubAction.Analyze -> {
                Log.d(
                    "AnalyzableSmsReceiver",
                    "SMS Analysis requested by the user from notification with ID $notificationId"
                )
                // TODO: Add functionality to analyze SMS.
            }
            SmsAnalysis.SubAction.Cancel -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }
}