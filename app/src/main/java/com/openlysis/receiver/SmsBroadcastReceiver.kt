package com.openlysis.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.openlysis.notification.Notifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val EXTRA_SMS_FORMAT = "format"

@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var notifier: Notifier

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val smsFormat = intent.getStringExtra(EXTRA_SMS_FORMAT)
        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        if (smsFormat == null) {
            Log.e("SmsBroadcastReceiver", "SMS message format was null.")
            return
        }

        smsMessages.forEach {
            notifier.notifyAnalyzableSms(it, smsFormat)
        }
    }
}