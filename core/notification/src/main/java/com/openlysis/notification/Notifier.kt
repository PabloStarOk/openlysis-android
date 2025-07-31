package com.openlysis.notification

import android.app.Notification
import android.content.Intent
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.Message

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
     * Notifies that the analysis of a message has been finalized.
     *
     * @param messageSender The sender of the message.
     * @param analysisStatus The final status of the analysis.
     * @param analysisVerdict The verdict of the analysis.
     */
    fun notifyMessageAnalysisFinalization(
        messageSender: String,
        analysisStatus: AnalysisStatus,
        analysisVerdict: Verdict
    )

    /**
     * Creates a notification of a message analysis based on its state.
     *
     * @param messageSender The sender of the message.
     * @param analysisStatus The status of the analysis.
     * @param analysisVerdict The verdict of the analysis.
     * @return A [Notification] representing for the specified message analysis state.
     */
    fun createMessageAnalysisNotification(
        messageSender: String,
        analysisStatus: AnalysisStatus,
        analysisVerdict: Verdict
    ): Notification
}