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
import com.openlysis.data.analysis.core.di.SmsAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.work.constant.SmsAnalysisWorkers.DEFAULT_INVALID_NOTIFICATION_ID
import com.openlysis.data.work.constant.SmsAnalysisWorkers.NOTIFICATION_ID_KEY
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
        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
                val notificationId =
                    inputData.getInt(NOTIFICATION_ID_KEY, DEFAULT_INVALID_NOTIFICATION_ID)
                if (notificationId == DEFAULT_INVALID_NOTIFICATION_ID) {
                    throw IllegalStateException("Notification ID was not found.")
                }

                val messageSender =
                    inputData.getString(MESSAGE_SENDER_KEY) ?: return@withContext Result.failure()
                val analysisId =
                    inputData.getString(ANALYSIS_ID_KEY) ?: return@withContext Result.failure()

                pollAnalysis(messageSender, analysisId)
            }

        private suspend fun pollAnalysis(
            messageSender: String,
            analysisId: String
        ): Result {
            var isResultFinal = false
            var verdict = Verdict.Unknown
            var status = AnalysisStatus.Queued
            while (!isResultFinal) {
                val outcome = smsRepository.getUpdatedById(analysisId)
                if (outcome !is Outcome.Success) {
                    val failure = outcome as Outcome.Failure
                    notifier.notifyMessageAnalysis(
                        messageSender,
                        AnalysisStatus.Failed,
                        Verdict.Unknown,
                        buildTapIntent(analysisId)
                    )
                    Log.e(LOGGING_TAG, "Analysis failed due to a ${failure.error}")
                    return Result.failure()
                }

                verdict = outcome.value.verdict
                status = outcome.value.status
                isResultFinal =
                    status != AnalysisStatus.Queued &&
                    status != AnalysisStatus.InProgress
                if (!isResultFinal) {
                    delay(POLLING_FREQUENCY_MS)
                }
            }

            notifier.notifyMessageAnalysis(
                messageSender,
                status,
                verdict,
                buildTapIntent(analysisId)
            )
            return Result.success()
        }

        override suspend fun getForegroundInfo(): ForegroundInfo {
            val notificationId =
                inputData.getInt(NOTIFICATION_ID_KEY, DEFAULT_INVALID_NOTIFICATION_ID)
            if (notificationId == DEFAULT_INVALID_NOTIFICATION_ID) {
                throw IllegalStateException("Notification ID was not found.")
            }

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