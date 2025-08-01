package com.openlysis.feature.tools

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.common.AnalysisSettings
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.AnalysisToolScreenViewModel
import com.openlysis.feature.tools.model.AttachedFileData
import com.openlysis.feature.tools.model.AttachedFileError
import com.openlysis.feature.tools.model.FileAttachmentSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for the File Analysis Tool screen.
 *
 * @property analysisSettings Settings for file analysis.
 * @property attachmentFactory Factory for creating file attachments.
 * @property fileRepository Repository for performing file analyses.
 */
@HiltViewModel
internal class FileAnalysisToolScreenViewModel
    @Inject
    constructor(
        private val analysisSettings: AnalysisSettings,
        attachmentSettings: FileAttachmentSettings,
        private val attachmentFactory: AttachmentFactory,
        private val fileRepository: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>
    ) : AnalysisToolScreenViewModel() {
        private val _uiState = MutableStateFlow(FileAnalysisToolUiState())
        val uiState = _uiState.asStateFlow()
        val attachmentSettings = attachmentSettings.copy(maxFilesAmount = 1)

        /**
         * Adds a file to the UI state after validating its size.
         *
         * @param file The file data to be attached.
         */
        fun addFile(file: AttachedFileData) {
            _uiState.update {
                val error =
                    when {
                        file.size < 1 -> AttachedFileError.NoData
                        file.size > attachmentSettings.maxFileSize -> AttachedFileError.TooLarge
                        else -> null
                    }

                val validatedFile = if (error == null) file else file.copy(error = error)
                it.copy(
                    file = validatedFile,
                    isFileAttached = true,
                    canRequestAnalysis = error == null
                )
            }
        }

        /**
         * Updates the password of the currently attached file.
         *
         * @param newPassword The new password to set for the file.
         */
        fun updateFilePassword(newPassword: String) {
            val file = uiState.value.file as AttachedFileData
            _uiState.update {
                it.copy(file = file.copy(password = newPassword))
            }
        }

        /**
         * Removes the currently attached file.
         */
        fun removeFile() {
            if (uiState.value.file == null) return
            _uiState.update {
                it.copy(
                    file = null,
                    isFileAttached = false,
                    canRequestAnalysis = false
                )
            }
        }

        override suspend fun handleStartAnalysis() {
            val file = uiState.value.file as AttachedFileData
            _uiState.update { it.copy(requestState = AnalysisRequestState.InProgress) }
            val outcome =
                attachmentFactory.create(
                    file.uri,
                    file.password
                )

            val attachment =
                when (outcome) {
                    is Outcome.Success -> outcome.value
                    is Outcome.Failure -> {
                        _uiState.update {
                            it.copy(requestState = AnalysisRequestState.Failure(outcome.error))
                        }
                        return
                    }
                }

            val request =
                AnalyzeFile(
                    attachment = attachment,
                    reanalyze = analysisSettings.reanalyzeFiles
                )

            try {
                val outcome = fileRepository.analyze(request)
                _uiState.update {
                    val requestState =
                        when (outcome) {
                            is Outcome.Success -> AnalysisRequestState.Success.File(outcome.value)
                            is Outcome.Failure -> AnalysisRequestState.Failure(outcome.error)
                        }
                    it.copy(requestState = requestState)
                }
            } finally {
                attachment.close()
            }
        }

        override fun onCancelRequest() {
            _uiState.update { it.copy(requestState = AnalysisRequestState.None) }
        }
    }