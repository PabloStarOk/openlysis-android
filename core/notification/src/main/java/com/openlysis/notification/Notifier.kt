package com.openlysis.notification

import android.telephony.SmsMessage

/**
 * Interface for notifying.
 */
interface Notifier {
    /**
     * Notifies that an SMS message is analyzable.
     *
     * @param message The SMS message to be analyzed.
     */
    fun notifyAnalyzableSms(message: SmsMessage)
}