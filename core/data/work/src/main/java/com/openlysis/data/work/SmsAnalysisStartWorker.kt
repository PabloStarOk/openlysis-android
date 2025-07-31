package com.openlysis.data.work

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.di.SmsAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.Message
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.AnalysisSettings
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.notification.Notifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.random.Random

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
        @SmsAnalysesRepository private val smsRepository:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val analysisSettings: AnalysisSettings,
        private val notifier: Notifier
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
                val pdu =
                    inputData.getByteArray(SMS_MESSAGE_PDU_KEY)
                        ?: return@withContext Result.failure()
                val format =
                    inputData.getString(SMS_MESSAGE_FORMAT_KEY)
                        ?: return@withContext Result.failure()
                val smsMessage = SmsMessage.createFromPdu(pdu, format)

                val request = createAnalysisRequest(smsMessage)
                val outcome = smsRepository.analyze(request)

                when (outcome) {
                    is Outcome.Success -> {
                        val sender = outcome.value.message.sender
                        val analysisId = outcome.value.id
                        Result.success(
                            workDataOf(
                                SmsAnalysisRefreshWorker.MESSAGE_SENDER_KEY to sender,
                                SmsAnalysisRefreshWorker.ANALYSIS_ID_KEY to analysisId
                            )
                        )
                    }
                    is Outcome.Failure -> {
                        notifier.notifyMessageAnalysisFinalization(
                            smsMessage.displayOriginatingAddress,
                            AnalysisStatus.Failed,
                            Verdict.Unknown
                        )
                        Log.e(LOGGING_TAG, "Analysis failed due to a ${outcome.error}")
                        Result.failure()
                    }
                }
            }

        override suspend fun getForegroundInfo(): ForegroundInfo {
            val notificationId = Random.nextInt()
            val notification =
                notifier.createMessageAnalysisNotification(
                    "",
                    AnalysisStatus.Queued,
                    Verdict.Unknown
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

        private fun createAnalysisRequest(smsMessage: SmsMessage): AnalyzeMessage {
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

            return AnalyzeMessage(
                message = message,
                reanalyze = analysisSettings.reanalyzeSms,
                countryCode = analysisSettings.defaultCountryCode
            )
        }

        companion object {
            const val SMS_MESSAGE_PDU_KEY = "smsMessagePdu"
            const val SMS_MESSAGE_FORMAT_KEY = "smsMessageFormat"
            private const val LOGGING_TAG = "SmsAnalysisSWorker"
        }
    }