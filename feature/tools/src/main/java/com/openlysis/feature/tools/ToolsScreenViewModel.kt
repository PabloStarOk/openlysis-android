package com.openlysis.feature.tools

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.request.AnalyzeUrl
import com.openlysis.data.analysis.core.request.Attachment
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.attachment.AttachmentFactory
import com.openlysis.feature.tools.data.AnalysisRequestState
import com.openlysis.feature.tools.data.AnalysisSettings
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings
import com.openlysis.feature.tools.data.ToolsDataSource
import com.openlysis.feature.tools.data.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URI
import java.net.URISyntaxException
import javax.inject.Inject

/**
 * ViewModel responsible for managing message, file and URL analyses functionality in the tools screen.
 *
 * @property attachmentFactory Factory for creating [Attachment] objects from [AttachedFileData] objects
 * @property fileAnalysisRepo Repository for analyzing individual files
 * @property urlAnalysisRepo Repository for analyzing URLs
 * @property fileAttachmentSettings Settings for file attachments configuration
 * @property analysisSettings Settings for analysis configuration
 */
@HiltViewModel
internal class ToolsScreenViewModel
    @Inject
    constructor(
        private val attachmentFactory: AttachmentFactory,
        private val fileAnalysisRepo: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        private val urlAnalysisRepo: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>,
        val fileAttachmentSettings: FileAttachmentSettings,
        val analysisSettings: AnalysisSettings
    ) : ViewModel() {
        val toolsRepository: ToolsRepository = ToolsDataSource()

        private val _currentAnalysisRequest =
            MutableStateFlow<AnalysisRequestState>(AnalysisRequestState.None)

        private var currentAnalysisRequestJob: Job? = null

        val currentAnalysisRequest = _currentAnalysisRequest.asStateFlow()

        /**
         * Initiates the analysis of a single file.
         *
         * The result of the analysis request can be observed from [currentAnalysisRequest].
         *
         * @param attachedFile Data object containing the URI and password (if any) of the file to analyze
         */
        fun startFileAnalysis(attachedFile: AttachedFileData) {
            _currentAnalysisRequest.value = AnalysisRequestState.InProgress
            val outcome =
                attachmentFactory.create(
                    attachedFile.uri,
                    attachedFile.password
                )

            val attachment =
                when (outcome) {
                    is Outcome.Success -> outcome.value
                    is Outcome.Failure -> {
                        _currentAnalysisRequest.value = AnalysisRequestState.Failure(outcome.error)
                        return
                    }
                }

            val request =
                AnalyzeFile(
                    attachment = attachment,
                    reanalyze = analysisSettings.reanalyzeFiles
                )

            currentAnalysisRequestJob =
                viewModelScope.launch {
                    try {
                        val outcome = fileAnalysisRepo.analyze(request)
                        _currentAnalysisRequest.value =
                            when (outcome) {
                                is Outcome.Success ->
                                    AnalysisRequestState.Success.File(
                                        outcome.value
                                    )

                                is Outcome.Failure -> AnalysisRequestState.Failure(outcome.error)
                            }
                    } finally {
                        attachment.close()
                    }
                }
            currentAnalysisRequestJob?.invokeOnCompletion { currentAnalysisRequestJob = null }
        }

        /**
         * Initiates the analysis of a URL.
         *
         * The result of the analysis request can be observed from [currentAnalysisRequest].
         *
         * @param url The URI to be analyzed
         */
        fun startUrlAnalysis(url: URI) {
            _currentAnalysisRequest.value = AnalysisRequestState.InProgress
            val request =
                AnalyzeUrl(
                    url = url,
                    reanalyze = analysisSettings.reanalyzeUrls
                )

            currentAnalysisRequestJob =
                viewModelScope.launch {
                    val outcome = urlAnalysisRepo.analyze(request)
                    _currentAnalysisRequest.value =
                        when (outcome) {
                            is Outcome.Success -> AnalysisRequestState.Success.Url(outcome.value)
                            is Outcome.Failure -> AnalysisRequestState.Failure(outcome.error)
                        }
                }

            currentAnalysisRequestJob?.invokeOnCompletion { currentAnalysisRequestJob = null }
        }

        /**
         * Cancels the currently running analysis request if one exists.
         */
        fun cancelCurrentRequest() {
            currentAnalysisRequestJob?.cancel()
            _currentAnalysisRequest.value = AnalysisRequestState.None
        }

        /**
         * Validates and converts a raw URL string into a URI object.
         * First checks if the URL matches a predefined regex pattern, then attempts to create a URI.
         *
         * @param rawUrl The URL string to validate and convert
         * @return A valid [URI] object if the URL is valid and can be parsed, null otherwise
         */
        fun getUrlIfValid(rawUrl: String): URI? {
            if (!Patterns.WEB_URL.matcher(rawUrl).matches()) {
                return null
            }

            try {
                val url = URI(rawUrl)
                return url
            } catch (_: URISyntaxException) {
                return null
            }
        }
    }