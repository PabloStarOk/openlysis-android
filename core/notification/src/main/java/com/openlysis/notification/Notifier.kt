package com.openlysis.notification

import android.telephony.SmsMessage

/**
 * Interface for notifying.
 */
interface Notifier {
    /**
     * Notifies that an SMS message is analyzable.
     *
     * @param sms The SMS message to be analyzed.
     * @param smsFormat The format of the SMS message.
     */
    fun notifyAnalyzableSms(
        sms: SmsMessage,
        smsFormat: String
    )
}