package com.openlysis.notification.constant

/**
 * Contains constants related to notification handling within the application.
 */
internal object Notifications {
    /**
     * Notification channel ID for SMS analysis notifications.
     */
    const val SMS_ANALYSIS_NOTIFICATION_CHANNEL_ID = "com.openlysis.SMS_ANALYSIS_NOTIFICATIONS"

    /**
     * Placeholder for notification data URI, expects a string argument to be formatted.
     */
    const val NOTIFICATION_DATA_URI_PLACEHOLDER = "app://openlysis/notification/%s"
}