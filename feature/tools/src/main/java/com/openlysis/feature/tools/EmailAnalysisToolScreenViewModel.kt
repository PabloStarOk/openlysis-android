package com.openlysis.feature.tools

import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.di.EmailAnalysesRepository
import com.openlysis.data.analysis.model.common.AnalysisSettings
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.request.Attachment
import com.openlysis.data.analysis.request.Message
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.AnalysisToolScreenViewModel
import com.openlysis.feature.tools.model.AttachedFileData
import com.openlysis.feature.tools.model.AttachedFileError
import com.openlysis.feature.tools.model.FileAttachmentSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

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
                val alreadyAttached =
                    it.attachedFiles.any { existing -> existing.value.uri == newFile.uri }
                var error =
                    when {
                        alreadyAttached -> AttachedFileError.AlreadyAttached
                        newFile.size < 1 -> AttachedFileError.NoData
                        newFile.size > attachmentSettings.maxFileSize -> AttachedFileError.TooLarge
                        it.attachedFiles.size >= attachmentSettings.maxFilesAmount ->
                            AttachedFileError.LimitReached
                        else -> null
                    }

                val validatedFile = newFile.copy(error = error)
                val updatedAttachedFiles = it.attachedFiles + Pair(newFile.id, validatedFile)
                it.copy(
                    attachedFiles = updatedAttachedFiles,
                    canAttachFiles = updatedAttachedFiles.size < attachmentSettings.maxFilesAmount
                )
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
            if (!uiState.value.attachedFiles.containsKey(file.id)) {
                return
            }

            _uiState.update {
                val mutableAttachedFiles = it.attachedFiles.toMutableMap()
                mutableAttachedFiles[file.id] = file.copy(password = newPassword)
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
                mutableAttachedFiles.remove(file.id)
                it.copy(
                    attachedFiles = mutableAttachedFiles,
                    canAttachFiles = mutableAttachedFiles.size < attachmentSettings.maxFilesAmount
                )
            }
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
            uiState
                .distinctUntilChanged { old, new ->
                    old.message == new.message && old.attachedFiles == new.attachedFiles
                }.onEach {
                    _uiState.update {
                        val canRequestAnalysis =
                            it.message.requiredFieldsSatisfied &&
                                it.attachedFiles.all { it.value.error == null }
                        it.copy(canRequestAnalysis = canRequestAnalysis)
                    }
                }.launchIn(viewModelScope)
        }
    }