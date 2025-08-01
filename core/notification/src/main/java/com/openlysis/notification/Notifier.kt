package com.openlysis.notification

import android.app.Notification
import android.content.Intent
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.Message
import kotlin.random.Random

/**
 * Interface for notifying.
 */
interface Notifier {
    /**
     * Notifies that an SMS message is analyzable.
     *
     * @param message The SMS message to be analyzed.
     * @param notificationId An [Int] to identify the notification.
     * @param analyzeIntent An [Intent] to execute when user requests an analysis for the SMS from the notification.
     * @param cancelIntent An [Intent] to execute when user requests to cancel the notification.
     * @param tapIntent An [Intent] to execute when the user taps the notification.
     */
    fun notifyAnalyzableSms(
        message: Message,
        notificationId: Int,
        analyzeIntent: Intent,
        cancelIntent: Intent,
        tapIntent: Intent
    )

    /**
     * Notifies that the status of a message analysis.
     *
     * @param messageSender The sender of the message.
     * @param analysisStatus The final status of the analysis.
     * @param analysisVerdict The verdict of the analysis.
     * @param tapIntent An optional [Intent] to execute when the user taps the notification.
     * @param notificationId An optional [Int] to use as the ID of the notification.
     */
    fun notifyMessageAnalysis(
        messageSender: String,
        analysisStatus: AnalysisStatus,
        analysisVerdict: Verdict,
        tapIntent: Intent?,
        notificationId: Int = Random.nextInt()
    )

    /**
     * Creates a notification of a message analysis based on its state.
     *
     * @param messageSender The sender of the message.
     * @param analysisStatus The status of the analysis.
     * @param analysisVerdict The verdict of the analysis.
     * @param tapIntent An optional [Intent] to execute when the user taps the notification.
     * @return A [Notification] representing for the specified message analysis state.
     */
    fun createMessageAnalysisNotification(
        messageSender: String,
        analysisStatus: AnalysisStatus,
        analysisVerdict: Verdict,
        tapIntent: Intent?
    ): Notification
}