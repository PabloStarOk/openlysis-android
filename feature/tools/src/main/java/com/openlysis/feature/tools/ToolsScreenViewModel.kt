package com.openlysis.feature.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.Attachment
import com.openlysis.data.analysis.core.request.Message
import com.openlysis.data.analysis.model.common.AnalysisError
import com.openlysis.data.analysis.model.common.Outcome
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.feature.tools.components.MessageSectionState
import com.openlysis.feature.tools.data.AnalysisSettings
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings
import com.openlysis.feature.tools.data.ToolsDataSource
import com.openlysis.feature.tools.data.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing email analysis functionality in the tools screen.
 *
 * @property attachmentFactory Factory for creating [Attachment] objects from [AttachedFileData] objects
 * @property messageAnalysisRepo Repository for analyzing email messages
 * @property fileAttachmentSettings Settings for file attachments configuration
 * @property analysisSettings Settings for analysis configuration
 */
@HiltViewModel
internal class ToolsScreenViewModel
    @Inject
    constructor(
        private val attachmentFactory: AttachmentFactory,
        private val messageAnalysisRepo: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        val fileAttachmentSettings: FileAttachmentSettings,
        val analysisSettings: AnalysisSettings
    ) : ViewModel() {
        val toolsRepository: ToolsRepository = ToolsDataSource()

        /**
         * Initiates the analysis of an email message with optional attachments.
         *
         * @param messageState The state containing email message details (sender, subject, content)
         * @param attachedFiles Optional list of files attached to the email
         * @param onSuccess Callback invoked when analysis completes successfully, providing the [MessageAnalysis] result
         * @param onError Callback invoked when an error occurs during analysis, providing the [AnalysisError] details
         */
        fun startEmailAnalysis(
            messageState: MessageSectionState,
            attachedFiles: List<AttachedFileData>?,
            onSuccess: (MessageAnalysis) -> Unit,
            onError: (AnalysisError) -> Unit
        ) {
            val attachments = mutableListOf<Attachment>()
            attachedFiles?.forEach {
                val outcome =
                    attachmentFactory.create(
                        it.uri,
                        it.password
                    )

                when (outcome) {
                    is Outcome.Success -> attachments.add(outcome.value)
                    is Outcome.Failure -> {
                        onError(outcome.error)
                        return
                    }
                }
            }

            val message =
                Message(
                    type = MessageType.Email,
                    sender = messageState.sender,
                    subject = messageState.subject,
                    content = messageState.content,
                    attachments = attachments
                )

            val request =
                AnalyzeMessage(
                    message = message,
                    reanalyze = analysisSettings.reanalyzeEmails,
                    countryCode = analysisSettings.defaultCountryCode
                )

            viewModelScope.launch {
                try {
                    val outcome = messageAnalysisRepo.analyze(request)
                    when (outcome) {
                        is Outcome.Success -> onSuccess(outcome.value)
                        is Outcome.Failure -> onError(outcome.error)
                    }
                } finally {
                    for (closeable in attachments) {
                        closeable.close()
                    }
                }
            }
        }
    }