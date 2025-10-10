package com.openlysis.data.work.constant

/**
 * Constants used for SMS analysis worker operations.
 */
object SmsAnalysisWorkers {
    /** Key for passing notification ID in worker input data. */
    const val NOTIFICATION_ID_KEY = "notificationId"

    /** Default value for notification ID which represents an invalid ID. */
    const val DEFAULT_INVALID_NOTIFICATION_ID = Int.MIN_VALUE
}