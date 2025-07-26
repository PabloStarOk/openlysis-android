package com.openlysis.feature.tools

import android.util.Patterns
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeUrl
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.feature.tools.data.AnalysisRequestState
import com.openlysis.feature.tools.data.AnalysisSettings
import com.openlysis.feature.tools.model.AnalysisToolScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URI
import javax.inject.Inject

/**
 * ViewModel for the URL analysis tool screen.
 *
 * @property analysisSettings Settings for analysis operations.
 * @property urlRepository Repository to perform URL analyses.
 */
@HiltViewModel
internal class UrlAnalysisToolScreenViewModel
    @Inject
    constructor(
        private val analysisSettings: AnalysisSettings,
        private val urlRepository: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>
    ) : AnalysisToolScreenViewModel() {
        private val _uiState = MutableStateFlow(UrlAnalysisToolUiState())
        val uiState = _uiState.asStateFlow()

        /**
         * Updates the URL in the UI state and validates it.
         *
         * @param url The URL string to be updated and validated.
         */
        fun updateUrl(url: String) {
            _uiState.update {
                val isValidUrl = Patterns.WEB_URL.matcher(url).matches()
                it.copy(
                    url = url,
                    isValidUrl = isValidUrl,
                    canRequestAnalysis = isValidUrl
                )
            }
        }

        override suspend fun handleStartAnalysis() {
            if (!uiState.value.isValidUrl) return

            val request =
                AnalyzeUrl(
                    url = URI.create(uiState.value.url),
                    reanalyze = analysisSettings.reanalyzeUrls
                )

            val outcome = urlRepository.analyze(request)
            val requestState =
                when (outcome) {
                    is Outcome.Success -> AnalysisRequestState.Success.Url(outcome.value)
                    is Outcome.Failure -> AnalysisRequestState.Failure(outcome.error)
                }
            _uiState.update { it.copy(requestState = requestState) }
        }

        override fun onCancelRequest() {
            _uiState.update { it.copy(requestState = AnalysisRequestState.None) }
        }
    }