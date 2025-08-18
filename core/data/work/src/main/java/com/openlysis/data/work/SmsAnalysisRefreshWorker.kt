package com.openlysis.data.work

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.openlysis.core.link.DeepLinks
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.di.SmsAnalysesRepository
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.work.utils.extractNotificationId
import com.openlysis.data.work.utils.getPersistentNotificationId
import com.openlysis.notification.Notifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Worker for refreshing SMS messages analyses.
 *
 * @param context The application context.
 * @param workerParameters Parameters for the worker.
 * @param coroutineDispatcher Dispatcher for coroutine execution.
 * @param smsRepository Repository for SMS analyses.
 * @param notifier Notifier for sending notifications.
 */
@HiltWorker
class SmsAnalysisRefreshWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParameters: WorkerParameters,
        @Dispatcher(AppDispatcher.IO) private val coroutineDispatcher: CoroutineDispatcher,
        @SmsAnalysesRepository private val smsRepository:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val notifier: Notifier
    ) : CoroutineWorker(context, workerParameters) {
        private var foregroundSet: Boolean = false

        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
                val notificationId = inputData.extractNotificationId()

                val messageSender =
                    inputData.getString(MESSAGE_SENDER_KEY) ?: return@withContext Result.failure()
                val analysisId =
                    inputData.getString(ANALYSIS_ID_KEY) ?: return@withContext Result.failure()

                if (!foregroundSet) {
                    notifier.notifyMessageAnalysis(
                        notificationId = notificationId,
                        messageSender = messageSender,
                        analysisStatus = AnalysisStatus.Queued,
                        analysisVerdict = Verdict.Unknown,
                        tapIntent = null
                    )
                }

                val pollOutcome = pollAnalysis(analysisId)
                val notificationTapIntent = buildTapIntent(analysisId)
                val persistentNotificationId = getPersistentNotificationId(foregroundSet)

                when (pollOutcome) {
                    is Outcome.Success -> {
                        val analysis = pollOutcome.value
                        notifier.notifyMessageAnalysis(
                            notificationId = persistentNotificationId,
                            messageSender = messageSender,
                            analysisStatus = analysis.status,
                            analysisVerdict = analysis.verdict,
                            tapIntent = notificationTapIntent
                        )
                        Result.success()
                    }
                    is Outcome.Failure -> {
                        Log.e(LOGGING_TAG, "Analysis failed due to a ${pollOutcome.error}")
                        notifier.notifyMessageAnalysisError(
                            notificationId = persistentNotificationId,
                            error = pollOutcome.error,
                            occurredOnStart = false,
                            messageSender = messageSender,
                            tapIntent = notificationTapIntent
                        )
                        Result.failure()
                    }
                }
            }

        private suspend fun pollAnalysis(analysisId: String): Outcome<MessageAnalysis> {
            var isResultFinal = false
            var verdict = Verdict.Unknown
            var status = AnalysisStatus.Queued
            var outcome: Outcome<MessageAnalysis>? = null

            while (!isResultFinal) {
                outcome = smsRepository.getUpdatedById(analysisId)
                if (outcome !is Outcome.Success) return outcome

                verdict = outcome.value.verdict
                status = outcome.value.status
                isResultFinal =
                    status != AnalysisStatus.Queued &&
                    status != AnalysisStatus.InProgress

                if (!isResultFinal) {
                    delay(POLLING_FREQUENCY_MS)
                }
            }

            return outcome as Outcome<MessageAnalysis>
        }

        override suspend fun getForegroundInfo(): ForegroundInfo {
            foregroundSet = true
            val notificationId = inputData.extractNotificationId()

            val messageSender = inputData.getString(MESSAGE_SENDER_KEY)
            if (messageSender == null) {
                throw IllegalStateException("SMS message sender was not found.")
            }

            val analysisId = inputData.getString(ANALYSIS_ID_KEY)
            if (analysisId == null) {
                throw IllegalStateException("Analysis ID was not found.")
            }

            val notification =
                notifier.createMessageAnalysisNotification(
                    messageSender,
                    AnalysisStatus.Queued,
                    Verdict.Unknown,
                    buildTapIntent(analysisId)
                )

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
        }

        private fun buildTapIntent(analysisId: String): Intent =
            Intent().apply {
                action = Intent.ACTION_VIEW
                data =
                    DeepLinks.Results.Message
                        .createUri(MessageType.Sms.toString(), analysisId)
                        .toUri()
                component =
                    ComponentName(applicationContext.packageName, DeepLinks.OPENLYSIS_ACTIVITY_NAME)
            }

        companion object {
            internal const val MESSAGE_SENDER_KEY = "messageSender"
            internal const val ANALYSIS_ID_KEY = "analysisId"
            private const val POLLING_FREQUENCY_MS = 3000L
            private const val LOGGING_TAG = "SmsAnalysisRWorker"
        }
    }