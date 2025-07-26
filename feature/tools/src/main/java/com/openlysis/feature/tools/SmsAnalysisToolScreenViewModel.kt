package com.openlysis.feature.tools

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.di.SmsAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.Message
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.AnalysisSettings
import com.openlysis.feature.tools.model.AnalysisToolScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for the SMS Analysis Tool screen.
 *
 * @param analysisSettings Provides configuration for analysis requests.
 * @param smsRepository Repository for performing SMS message analyses.
 */
@HiltViewModel
internal class SmsAnalysisToolScreenViewModel
    @Inject
    constructor(
        private val analysisSettings: AnalysisSettings,
        @SmsAnalysesRepository private val smsRepository:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>
    ) : AnalysisToolScreenViewModel() {
        private val _uiState = MutableStateFlow(SmsAnalysisToolUiState())
        val uiState = _uiState.asStateFlow()

        /**
         * Updates the sender field of the message in the UI state.
         *
         * @param sender The new sender value to set.
         */
        fun updateSender(sender: String) {
            _uiState.update {
                val updatedMessage = it.message.copy(sender = sender)
                it.copy(
                    message = updatedMessage,
                    canRequestAnalysis = updatedMessage.requiredFieldsSatisfied
                )
            }
        }

        /**
         * Updates the content field of the message in the UI state.
         *
         * @param content The new content value to set.
         */
        fun updateContent(content: String) {
            _uiState.update {
                val updatedMessage = it.message.copy(content = content)
                it.copy(
                    message = updatedMessage,
                    canRequestAnalysis = updatedMessage.requiredFieldsSatisfied
                )
            }
        }

        override suspend fun handleStartAnalysis() {
            _uiState.update { it.copy(requestState = AnalysisRequestState.InProgress) }
            val message =
                Message(
                    type = MessageType.Sms,
                    sender = uiState.value.message.sender,
                    subject = null,
                    content = uiState.value.message.content,
                    attachments = null
                )

            val request =
                AnalyzeMessage(
                    message = message,
                    reanalyze = analysisSettings.reanalyzeSms,
                    countryCode = analysisSettings.defaultCountryCode
                )

            val outcome = smsRepository.analyze(request)
            _uiState.update {
                when (outcome) {
                    is Outcome.Success ->
                        it.copy(requestState = AnalysisRequestState.Success.Message(outcome.value))

                    is Outcome.Failure ->
                        it.copy(requestState = AnalysisRequestState.Failure(outcome.error))
                }
            }
        }

        override fun onCancelRequest() {
            _uiState.update { it.copy(requestState = AnalysisRequestState.None) }
        }
    }