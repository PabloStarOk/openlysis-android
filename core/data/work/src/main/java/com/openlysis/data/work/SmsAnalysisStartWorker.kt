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
import androidx.work.workDataOf
import com.openlysis.core.link.DeepLinks
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.di.MessageAnalysisDependency
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.AnalysisSettings
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.request.Message
import com.openlysis.data.work.utils.extractNotificationId
import com.openlysis.data.work.utils.getPersistentNotificationId
import com.openlysis.notification.Notifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Worker for starting SMS messages analyses.
 *
 * @param context The application context.
 * @param workerParameters Parameters for the worker.
 * @param coroutineDispatcher Dispatcher for coroutine execution.
 * @param smsRepository Repository for SMS analysis operations.
 * @param analysisSettings Settings for SMS analysis.
 * @param notifier Notifier for message analysis notifications.
 */
@HiltWorker
class SmsAnalysisStartWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted workerParameters: WorkerParameters,
        @Dispatcher(AppDispatcher.IO) private val coroutineDispatcher: CoroutineDispatcher,
        @MessageAnalysisDependency(MessageType.Sms)
        private val smsRepository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val analysisSettings: AnalysisSettings,
        private val notifier: Notifier
    ) : CoroutineWorker(context, workerParameters) {
        private var foregroundSet: Boolean = false

        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
                val notificationId = inputData.extractNotificationId()

                val messageSender =
                    inputData.getString(MESSAGE_SENDER_KEY) ?: return@withContext Result.failure()
                val messageBody =
                    inputData.getString(MESSAGE_BODY_KEY) ?: return@withContext Result.failure()

                if (!foregroundSet) {
                    notifier.notifyMessageAnalysis(
                        notificationId = notificationId,
                        messageSender = messageSender,
                        analysisStatus = AnalysisStatus.Queued,
                        analysisVerdict = Verdict.Unknown,
                        tapIntent = null
                    )
                }

                val request = createAnalysisRequest(messageSender, messageBody)
                val outcome = smsRepository.analyze(request)

                when (outcome) {
                    is Outcome.Success -> {
                        val analysisId = outcome.value.id
                        Result.success(
                            workDataOf(
                                SmsAnalysisRefreshWorker.MESSAGE_SENDER_KEY to messageSender,
                                SmsAnalysisRefreshWorker.ANALYSIS_ID_KEY to analysisId
                            )
                        )
                    }
                    is Outcome.Failure -> {
                        Log.e(LOGGING_TAG, "Analysis failed due to a ${outcome.error}")
                        val persistentNotificationId = getPersistentNotificationId(foregroundSet)
                        notifier.notifyMessageAnalysisError(
                            notificationId = persistentNotificationId,
                            error = outcome.error,
                            occurredOnStart = true,
                            messageSender = messageSender,
                            tapIntent = buildTapIntent(messageSender, messageBody)
                        )
                        Result.failure()
                    }
                }
            }

        override suspend fun getForegroundInfo(): ForegroundInfo {
            foregroundSet = true
            val notificationId = inputData.extractNotificationId()

            val messageSender = inputData.getString(MESSAGE_SENDER_KEY)
            if (messageSender == null) {
                throw IllegalStateException("SMS message sender was not found.")
            }

            val notification =
                notifier.createMessageAnalysisNotification(
                    messageSender,
                    AnalysisStatus.Queued,
                    Verdict.Unknown,
                    null
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

        private fun createAnalysisRequest(
            sender: String,
            body: String
        ): AnalyzeMessage {
            val message =
                Message(
                    type = MessageType.Sms,
                    sender = sender,
                    subject = null,
                    content = body,
                    attachments = null
                )

            return AnalyzeMessage(
                message = message,
                reanalyze = analysisSettings.reanalyzeSms,
                countryCode = analysisSettings.defaultCountryCode
            )
        }

        private fun buildTapIntent(
            sender: String,
            content: String
        ): Intent =
            Intent().apply {
                action = Intent.ACTION_VIEW
                data =
                    DeepLinks.Tools.Sms
                        .createUri(sender, content)
                        .toUri()
                component =
                    ComponentName(applicationContext.packageName, DeepLinks.OPENLYSIS_ACTIVITY_NAME)
            }

        companion object {
            /** Key for the sender of the message in input data. */
            const val MESSAGE_SENDER_KEY = "messageSender"

            /** Key for the body of the message in input data. */
            const val MESSAGE_BODY_KEY = "messageBody"

            private const val LOGGING_TAG = "SmsAnalysisSWorker"
        }
    }