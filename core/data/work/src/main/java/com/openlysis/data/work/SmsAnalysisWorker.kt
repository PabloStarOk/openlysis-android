package com.openlysis.data.work

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.di.SmsAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.Message
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.tools.model.AnalysisSettings
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Worker for analyzing SMS messages in the background.
 *
 * Uses Hilt for dependency injection.
 *
 * @param context The application context.
 * @param workerParameters Parameters for the worker.
 * @param coroutineDispatcher Dispatcher for coroutine execution.
 * @param smsRepository Repository for SMS analysis operations.
 * @param analysisSettings Settings for SMS analysis.
 */
@HiltWorker
class SmsAnalysisWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParameters: WorkerParameters,
        @Dispatcher(AppDispatcher.IO) private val coroutineDispatcher: CoroutineDispatcher,
        @SmsAnalysesRepository private val smsRepository:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val analysisSettings: AnalysisSettings
    ) : CoroutineWorker(context, workerParameters) {
        private var notificationId: Int = -1

        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
                notificationId = inputData.getInt(NOTIFICATION_ID_KEY, notificationId)
                if (notificationId == -1) {
                    Log.e(LOGGING_TAG, "Notification ID was not found.")
                    return@withContext Result.failure()
                }

                val pdu =
                    inputData.getByteArray(SMS_MESSAGE_PDU_KEY)
                        ?: return@withContext Result.failure()
                val format =
                    inputData.getString(SMS_MESSAGE_FORMAT_KEY)
                        ?: return@withContext Result.failure()
                val smsMessage = SmsMessage.createFromPdu(pdu, format)

                val outcome = startAnalysis(smsMessage)
                if (outcome !is Outcome.Success) {
                    return@withContext Result.failure()
                }
                pollAnalysis(
                    messageSender = smsMessage.displayOriginatingAddress,
                    analysisId = outcome.value.id
                )
            }

        override suspend fun getForegroundInfo(): ForegroundInfo {
            val notification =
                notificationBuilder {
                    setContentTitle(
                        applicationContext.getString(
                            R.string.worker_notification_title_sms_analysis_starting
                        )
                    )
                    setAutoCancel(false)
                    setOngoing(true)
                }

            return buildForegroundInfo(notificationId, notification)
        }

        private suspend fun startAnalysis(smsMessage: SmsMessage): Outcome<MessageAnalysis> {
            val sender =
                if (smsMessage.isEmail &&
                    smsMessage.emailFrom != null &&
                    smsMessage.emailFrom.isNotBlank()
                ) {
                    smsMessage.emailFrom
                } else {
                    smsMessage.originatingAddress
                }

            val content =
                if (smsMessage.isEmail &&
                    smsMessage.emailBody != null &&
                    smsMessage.emailBody.isNotBlank()
                ) {
                    smsMessage.emailBody
                } else {
                    smsMessage.messageBody
                }

            val message =
                Message(
                    type = MessageType.Sms,
                    sender = sender ?: "",
                    subject = smsMessage.pseudoSubject,
                    content = content,
                    attachments = null
                )

            val request =
                AnalyzeMessage(
                    message = message,
                    reanalyze = analysisSettings.reanalyzeSms,
                    countryCode = analysisSettings.defaultCountryCode
                )

            val outcome = smsRepository.analyze(request)
            if (outcome is Outcome.Failure) {
                onAnalysisFailure(messageSender = smsMessage.displayOriginatingAddress, outcome)
            }
            return outcome
        }

        private suspend fun pollAnalysis(
            messageSender: String,
            analysisId: String
        ): Result {
            notifyInProgress()
            var isResultFinal = false
            while (!isResultFinal) {
                delay(POLLING_FREQUENCY_MS)
                val outcome = smsRepository.getUpdatedById(analysisId)
                if (outcome !is Outcome.Success) {
                    onAnalysisFailure(messageSender, outcome as Outcome.Failure)
                    return Result.failure()
                }

                val status = outcome.value.status
                isResultFinal =
                    status != AnalysisStatus.Queued &&
                    status != AnalysisStatus.InProgress

                if (isResultFinal) {
                    notifyFinished(messageSender, status, outcome.value.verdict)
                }
            }

            return Result.success()
        }

        private suspend fun notifyInProgress() {
            val notification =
                notificationBuilder {
                    setContentTitle(
                        applicationContext.getString(
                            R.string.worker_notification_title_sms_analysis_in_progress
                        )
                    )
                    setContentText(
                        applicationContext.getString(
                            R.string.worker_notification_content_sms_analysis_in_progress
                        )
                    )
                    setProgress(0, 0, true)
                    setOngoing(true)
                }

            try {
                setForeground(buildForegroundInfo(notificationId, notification))
            } catch (ex: IllegalStateException) {
                Log.e(LOGGING_TAG, "Could not set the foreground.", ex)
            }
        }

        private fun notifyFinished(
            messageSender: String,
            status: AnalysisStatus,
            verdict: Verdict
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                applicationContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val notificationId = Random.nextInt()
            val notification =
                notificationBuilder {
                    // TODO: Add action to retry.
                    if (status == AnalysisStatus.Completed) {
                        val (title, rawText) = getCompletedAnalysisStringResources(verdict)
                        val formattedText = applicationContext.getString(rawText, messageSender)
                        setContentTitle(applicationContext.getString(title))
                        setContentText(formattedText)
                        setStyle(NotificationCompat.BigTextStyle().bigText(formattedText))
                    } else {
                        setContentTitle(
                            applicationContext.getString(
                                R.string.worker_notification_title_sms_analysis_failed
                            )
                        )
                        setContentText(
                            applicationContext.getString(
                                R.string.worker_notification_content_sms_analysis_failed
                            )
                        )
                    }
                    setAutoCancel(true)
                    setOngoing(false)
                }

            NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
        }

        // TODO: Add pending intent to use when user taps the notification.
        private fun notificationBuilder(
            customize: NotificationCompat.Builder.() -> NotificationCompat.Builder
        ): Notification =
            NotificationCompat
                .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(AppIconsIds.Openlysis)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .customize()
                .build()

        private fun getCompletedAnalysisStringResources(verdict: Verdict): Pair<Int, Int> =
            when (verdict) {
                Verdict.Unknown ->
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_generic,
                        R.string.worker_notification_content_sms_analysis_completed_unknown
                    )
                Verdict.Undetected ->
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_generic,
                        R.string.worker_notification_content_sms_analysis_completed_undetected
                    )
                Verdict.Suspicious ->
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_suspicious,
                        R.string.worker_notification_content_sms_analysis_completed_suspicious
                    )
                Verdict.Malicious ->
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_malicious,
                        R.string.worker_notification_content_sms_analysis_completed_malicious
                    )
            }

        private fun onAnalysisFailure(
            messageSender: String,
            failure: Outcome.Failure
        ) {
            notifyFinished(messageSender, AnalysisStatus.Failed, Verdict.Unknown)
            Log.e(LOGGING_TAG, "Analysis failed due to a ${failure.error}")
        }

        private fun buildForegroundInfo(
            notificationId: Int,
            notification: Notification
        ): ForegroundInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ForegroundInfo(
                    notificationId,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                ForegroundInfo(
                    notificationId,
                    notification
                )
            }

        companion object {
            const val NOTIFICATION_ID_KEY = "notificationId"
            const val SMS_MESSAGE_PDU_KEY = "smsMessagePdu"
            const val SMS_MESSAGE_FORMAT_KEY = "smsMessageFormat"
            private const val NOTIFICATION_CHANNEL_ID = "com.openlysis.SMS_ANALYSIS_NOTIFICATIONS"
            private const val POLLING_FREQUENCY_MS = 3000L
            private const val LOGGING_TAG = "SmsAnalysisWorker"
        }
    }