package com.openlysis.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.openlysis.notification.Notifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val EXTRA_SMS_FORMAT = "format"

/**
 * BroadcastReceiver that listens for incoming SMS messages.
 */
@AndroidEntryPoint
internal class SmsBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var notifier: Notifier

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val smsFormat = intent.getStringExtra(EXTRA_SMS_FORMAT)
        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        if (smsFormat == null) {
            throw IllegalStateException("Broadcast was received but SMS format was null.")
        }

        smsMessages.forEach {
            val sms = it
            val notificationId = sms.emailBody?.hashCode() ?: sms.messageBody.hashCode()
            val analyzeIntent =
                context.smsAnalysisIntent(
                    notificationId,
                    SmsAnalysisAvailableBroadcastReceiver.SubAction.Analyze,
                    sms,
                    smsFormat
                )
            val cancelIntent =
                context.smsAnalysisIntent(
                    notificationId,
                    SmsAnalysisAvailableBroadcastReceiver.SubAction.Cancel,
                    sms,
                    smsFormat
                )

            notifier.notifyAnalyzableSms(
                sms,
                smsFormat,
                notificationId,
                analyzeIntent,
                cancelIntent
            )
        }
    }

    private fun Context.smsAnalysisIntent(
        notificationId: Int,
        subAction: SmsAnalysisAvailableBroadcastReceiver.SubAction,
        sms: SmsMessage,
        smsFormat: String
    ): Intent =
        Intent(this, SmsAnalysisAvailableBroadcastReceiver::class.java).apply {
            action = SmsAnalysisAvailableBroadcastReceiver.SMS_ANALYSIS_AVAILABLE_INTENT
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_SUB_ACTION, subAction.toString())
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_SMS_PDU, sms.pdu)
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_SMS_FORMAT, smsFormat)
        }
}