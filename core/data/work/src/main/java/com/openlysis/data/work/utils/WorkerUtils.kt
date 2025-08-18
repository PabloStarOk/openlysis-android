package com.openlysis.data.work.utils

import androidx.work.CoroutineWorker
import androidx.work.Data
import com.openlysis.data.work.constant.SmsAnalysisWorkers.DEFAULT_INVALID_NOTIFICATION_ID
import com.openlysis.data.work.constant.SmsAnalysisWorkers.NOTIFICATION_ID_KEY
import kotlin.random.Random

/**
 * Extracts the notification ID from the input data.
 *
 * @return The notification ID as an `Int`.
 * @throws IllegalStateException if the notification ID is not found in the input data.
 */
internal fun Data.extractNotificationId(): Int {
    val notificationId =
        this.getInt(NOTIFICATION_ID_KEY, DEFAULT_INVALID_NOTIFICATION_ID)
    if (notificationId == DEFAULT_INVALID_NOTIFICATION_ID) {
        throw IllegalStateException("Notification ID was not found.")
    }

    return notificationId
}

/**
 * Returns a persistent notification ID for use with notifications.
 *
 * @param isForegroundSet If true, generates a random notification ID to persist the notification; otherwise, uses the original notification ID.
 *
 * If the worker has set foreground information (`foregroundSet` is true), a random notification ID is generated.
 * This ensures the notification remains persistent and is not automatically cancelled when the worker finishes.
 * Otherwise, returns the original notification ID extracted from the input data.
 *
 * This ensures the user is notified about the analysis status on any Android version.
 */
internal fun CoroutineWorker.getPersistentNotificationId(isForegroundSet: Boolean): Int {
    val notificationId = inputData.extractNotificationId()
    if (isForegroundSet) {
        var randomId: Int
        do {
            randomId = Random.nextInt()
        } while (randomId == notificationId)
        return randomId
    }
    return notificationId
}