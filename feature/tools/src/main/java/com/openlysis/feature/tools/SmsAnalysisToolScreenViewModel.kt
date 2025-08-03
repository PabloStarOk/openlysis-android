package com.openlysis.feature.tools

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.openlysis.core.link.DeepLinks
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.di.SmsAnalysesRepository
import com.openlysis.data.analysis.model.common.AnalysisSettings
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.request.Message
import com.openlysis.feature.tools.model.AnalysisRequestState
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
        savedStateHandle: SavedStateHandle,
        private val analysisSettings: AnalysisSettings,
        @SmsAnalysesRepository private val smsRepository:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>
    ) : AnalysisToolScreenViewModel() {
        private val _uiState = MutableStateFlow(SmsAnalysisToolUiState())
        val uiState = _uiState.asStateFlow()

        init {
            val deepLinkedSender =
                savedStateHandle.get<String?>(DeepLinks.Tools.Sms.ENCODED_SENDER_KEY)
            val deepLinkedContent =
                savedStateHandle.get<String?>(DeepLinks.Tools.Sms.ENCODED_CONTENT_KEY)
            if (deepLinkedSender != null) updateSender(Uri.decode(deepLinkedSender))
            if (deepLinkedContent != null) updateContent(Uri.decode(deepLinkedContent))
        }

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