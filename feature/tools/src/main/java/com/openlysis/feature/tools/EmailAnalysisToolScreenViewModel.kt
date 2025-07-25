package com.openlysis.feature.tools

import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.di.EmailAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.Attachment
import com.openlysis.data.analysis.core.request.Message
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.feature.tools.data.AnalysisRequestState
import com.openlysis.feature.tools.data.AnalysisSettings
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings
import com.openlysis.feature.tools.model.AnalysisToolScreenViewModel
import com.openlysis.feature.tools.model.AttachedFileError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.collections.forEach
import kotlin.collections.plus

/**
 * ViewModel for the Email Analysis Tool screen.
 *
 * @property analysisSettings Settings for analysis behavior.
 * @property attachmentSettings Settings for file attachment limits and sizes.
 * @property attachmentFactory Factory for creating Attachment instances.
 * @property emailRepository Repository for analyzing email messages.
 */
@HiltViewModel
internal class EmailAnalysisToolScreenViewModel
    @Inject
    constructor(
        private val analysisSettings: AnalysisSettings,
        val attachmentSettings: FileAttachmentSettings,
        private val attachmentFactory: AttachmentFactory,
        @EmailAnalysesRepository private val emailRepository:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>
    ) : AnalysisToolScreenViewModel() {
        private val _uiState =
            MutableStateFlow<EmailAnalysisToolUiState>(EmailAnalysisToolUiState())
        val uiState = _uiState.asStateFlow()

        init {
            validateRequestSubmission()
            validateAttachmentLimit()
        }

        /**
         * Updates the sender field in the message.
         *
         * @param sender The sender's email address.
         */
        fun updateSender(sender: String) {
            _uiState.update {
                val updatedMessage = it.message.copy(sender = sender)
                it.copy(message = updatedMessage)
            }
        }

        /**
         * Updates the subject field in the message.
         *
         * @param subject The subject of the email.
         */
        fun updateSubject(subject: String) {
            _uiState.update {
                val updatedMessage = it.message.copy(subject = subject)
                it.copy(message = updatedMessage)
            }
        }

        /**
         * Updates the content field in the message.
         *
         * @param content The content/body of the email.
         */
        fun updateContent(content: String) {
            _uiState.update {
                val updatedMessage = it.message.copy(content = content)
                it.copy(message = updatedMessage)
            }
        }

        /**
         * Attaches a new file to the email analysis request.
         * Validates for duplicate, empty, and too large files.
         *
         * @param newFile The file data to attach.
         */
        fun attachFile(newFile: AttachedFileData) {
            _uiState.update {
                if (it.attachedFiles.containsKey(newFile.uri)) {
                    addInvalidAttachedFile(AttachedFileError.AlreadyAttached, newFile)
                    return
                }

                if (newFile.size < 1) {
                    addInvalidAttachedFile(AttachedFileError.NoData, newFile)
                    return
                }

                if (newFile.size > attachmentSettings.maxFileSize) {
                    addInvalidAttachedFile(AttachedFileError.TooLarge, newFile)
                    return
                }

                it.copy(attachedFiles = it.attachedFiles + Pair(newFile.uri, newFile))
            }
        }

        /**
         * Updates the password for an attached file.
         *
         * @param file The file to update.
         * @param newPassword The new password for the file.
         */
        fun updateAttachedFilePassword(
            file: AttachedFileData,
            newPassword: String
        ) {
            _uiState.update {
                if (!it.attachedFiles.containsKey(file.uri)) {
                    it
                    return
                }

                val mutableAttachedFiles = it.attachedFiles.toMutableMap()
                mutableAttachedFiles[file.uri] = file.copy(password = newPassword)
                it.copy(attachedFiles = mutableAttachedFiles)
            }
        }

        /**
         * Detaches a file from the email analysis request.
         *
         * @param file The file data to detach.
         */
        fun detachFile(file: AttachedFileData) {
            _uiState.update {
                val mutableAttachedFiles = it.attachedFiles.toMutableMap()
                mutableAttachedFiles.remove(file.uri)
                it.copy(
                    attachedFiles = mutableAttachedFiles
                )
            }
        }

        /**
         * Clears the invalid attached files.
         */
        fun clearInvalidAttachedFiles() {
            _uiState.update { it.copy(invalidAttachedFiles = emptySet()) }
        }

        /**
         * Starts the analysis of the email message and its attachments.
         * Updates the UI state based on the analysis outcome.
         */
        override suspend fun handleStartAnalysis() {
            _uiState.update { it.copy(requestState = AnalysisRequestState.InProgress) }
            val attachments = mutableListOf<Attachment>()
            uiState.value.attachedFiles.values.forEach {
                val outcome =
                    attachmentFactory.create(
                        it.uri,
                        it.password
                    )

                when (outcome) {
                    is Outcome.Success -> attachments.add(outcome.value)
                    is Outcome.Failure -> {
                        _uiState.update {
                            it.copy(
                                requestState = AnalysisRequestState.Failure(outcome.error)
                            )
                        }
                        return
                    }
                }
            }

            val message =
                Message(
                    type = MessageType.Email,
                    sender = uiState.value.message.sender,
                    subject = uiState.value.message.subject,
                    content = uiState.value.message.content,
                    attachments = attachments
                )

            val request =
                AnalyzeMessage(
                    message = message,
                    reanalyze = analysisSettings.reanalyzeEmails,
                    countryCode = analysisSettings.defaultCountryCode
                )

            try {
                val outcome = emailRepository.analyze(request)
                _uiState.update {
                    when (outcome) {
                        is Outcome.Success ->
                            it.copy(
                                requestState =
                                    AnalysisRequestState.Success.Message(
                                        outcome.value
                                    )
                            )

                        is Outcome.Failure ->
                            it.copy(
                                requestState = AnalysisRequestState.Failure(outcome.error)
                            )
                    }
                }
            } finally {
                for (closeable in attachments) {
                    closeable.close()
                }
            }
        }

        override fun onCancelRequest() {
            _uiState.update { it.copy(requestState = AnalysisRequestState.None) }
        }

        private fun validateRequestSubmission() {
            _uiState
                .distinctUntilChangedBy { it.message }
                .onEach {
                    _uiState.update {
                        it.copy(canRequestAnalysis = it.message.submitEnabled)
                    }
                }.launchIn(viewModelScope)
        }

        private fun validateAttachmentLimit() {
            _uiState
                .distinctUntilChangedBy { it.attachedFiles }
                .onEach {
                    _uiState.update {
                        val canAttachFiles =
                            it.attachedFiles.size < attachmentSettings.maxFilesAmount
                        it.copy(canAttachFiles = canAttachFiles)
                    }
                }.launchIn(viewModelScope)
        }

        private fun addInvalidAttachedFile(
            error: AttachedFileError,
            file: AttachedFileData
        ) {
            _uiState.update {
                val fileWithError = file.copy(error = error)
                it.invalidAttachedFiles.toMutableSet().removeIf { existing ->
                    existing.uri == file.uri
                }

                it.copy(invalidAttachedFiles = it.invalidAttachedFiles + fileWithError)
            }
        }
    }