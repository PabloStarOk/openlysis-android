package com.openlysis.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.core.net.toUri
import com.openlysis.core.link.DeepLinks
import com.openlysis.data.analysis.model.message.Message
import com.openlysis.data.analysis.model.message.MessageType
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

        val unifiedMessages = smsMessages.toUnifiedMessages()
        val analyzableMessages = unifiedMessages.filter { it.content.isNotBlank() }
        analyzableMessages.forEach { postNotification(context, it) }
    }

    private fun postNotification(
        context: Context,
        message: Message
    ) {
        val notificationId = message.content.hashCode()
        val analyzeIntent =
            context.smsAnalysisIntent(
                notificationId,
                SmsAnalysisAvailableBroadcastReceiver.SubAction.Analyze,
                message
            )
        val cancelIntent =
            context.smsAnalysisIntent(
                notificationId,
                SmsAnalysisAvailableBroadcastReceiver.SubAction.Cancel,
                message
            )

        val encodedSender = Uri.encode(message.sender)
        val encodedContent = Uri.encode(message.content)
        val tapIntent =
            Intent().apply {
                action = Intent.ACTION_VIEW
                data =
                    DeepLinks.Tools.Sms
                        .createUri(encodedSender, encodedContent)
                        .toUri()
                component = ComponentName(context.packageName, DeepLinks.OPENLYSIS_ACTIVITY_NAME)
            }

        notifier.notifyAnalyzableSms(
            message,
            notificationId,
            analyzeIntent,
            cancelIntent,
            tapIntent
        )
    }

    private fun Context.smsAnalysisIntent(
        notificationId: Int,
        subAction: SmsAnalysisAvailableBroadcastReceiver.SubAction,
        message: Message
    ): Intent =
        Intent(this, SmsAnalysisAvailableBroadcastReceiver::class.java).apply {
            action = SmsAnalysisAvailableBroadcastReceiver.SMS_ANALYSIS_AVAILABLE_INTENT
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_SUB_ACTION, subAction.toString())
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_MESSAGE_SENDER, message.sender)
            putExtra(SmsAnalysisAvailableBroadcastReceiver.EXTRA_MESSAGE_BODY, message.content)
        }

    private fun Array<SmsMessage>.toUnifiedMessages(): List<Message> {
        val messagesBySender = this.groupBy(keySelector = { it.originatingAddress ?: "" })
        return messagesBySender.flatMap { group ->
            group.value
                .groupBy { msg -> msg.timestampMillis }
                .map {
                    val content = it.value.joinToString { it.messageBody ?: it.emailBody ?: "" }
                    Message(
                        type = MessageType.Sms,
                        sender = group.key,
                        content = content,
                        subject = null
                    )
                }
        }
    }
}