package com.openlysis.feature.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.Attachment
import com.openlysis.data.analysis.core.request.Message
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.common.AnalysisError
import com.openlysis.data.analysis.model.common.Outcome
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.feature.tools.components.MessageState
import com.openlysis.feature.tools.data.AnalysisSettings
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings
import com.openlysis.feature.tools.data.ToolsDataSource
import com.openlysis.feature.tools.data.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing message, file and URL analyses functionality in the tools screen.
 *
 * @property attachmentFactory Factory for creating [Attachment] objects from [AttachedFileData] objects
 * @property messageAnalysisRepo Repository for analyzing email messages
 * @property fileAnalysisRepo Repository for analyzing individual files
 * @property fileAttachmentSettings Settings for file attachments configuration
 * @property analysisSettings Settings for analysis configuration
 */
@HiltViewModel
internal class ToolsScreenViewModel
    @Inject
    constructor(
        private val attachmentFactory: AttachmentFactory,
        private val messageAnalysisRepo: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val fileAnalysisRepo: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        val fileAttachmentSettings: FileAttachmentSettings,
        val analysisSettings: AnalysisSettings
    ) : ViewModel() {
        val toolsRepository: ToolsRepository = ToolsDataSource()

        /**
         * Initiates the analysis of a message with optional attachments.
         *
         * @param type The type of message to be analyzed
         * @param messageState Current state of the message containing sender, subject and content
         * @param attachedFiles Optional list of files attached to the message
         * @param onSuccess Callback function to handle successful analysis with MessageAnalysis result
         * @param onError Callback function to handle analysis errors with AnalysisError
         */
        fun startMessageAnalysis(
            type: MessageType,
            messageState: MessageState,
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
                    type = type,
                    sender = messageState.sender,
                    subject = messageState.subject,
                    content = messageState.content,
                    attachments = attachments
                )

            val defaultReanalyze =
                when (type) {
                    MessageType.Email -> analysisSettings.reanalyzeEmails
                    MessageType.Sms -> analysisSettings.reanalyzeSms
                }

            val request =
                AnalyzeMessage(
                    message = message,
                    reanalyze = defaultReanalyze,
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

        /**
         * Initiates the analysis of a single file.
         *
         * @param attachedFile Data object containing the URI and password (if any) of the file to analyze
         * @param onSuccess Callback function to handle successful analysis with FileMultiAnalysis result
         * @param onError Callback function to handle analysis errors with AnalysisError
         */
        fun startFileAnalysis(
            attachedFile: AttachedFileData,
            onSuccess: (FileMultiAnalysis) -> Unit,
            onError: (AnalysisError) -> Unit
        ) {
            val outcome =
                attachmentFactory.create(
                    attachedFile.uri,
                    attachedFile.password
                )

            val attachment =
                when (outcome) {
                    is Outcome.Success -> outcome.value
                    is Outcome.Failure -> {
                        onError(outcome.error)
                        return
                    }
                }

            val request =
                AnalyzeFile(
                    attachment = attachment,
                    reanalyze = analysisSettings.reanalyzeFiles
                )

            viewModelScope.launch {
                try {
                    val outcome = fileAnalysisRepo.analyze(request)
                    when (outcome) {
                        is Outcome.Success -> onSuccess(outcome.value)
                        is Outcome.Failure -> onError(outcome.error)
                    }
                } finally {
                    attachment.close()
                }
            }
        }
    }