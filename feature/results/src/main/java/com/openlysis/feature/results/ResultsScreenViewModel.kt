package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.di.EmailAnalysesRepository
import com.openlysis.data.analysis.di.SmsAnalysesRepository
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.MultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeFile
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.request.AnalyzeUrl
import com.openlysis.feature.results.components.VerdictStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the initial results screen.
 *
 * @property emailAnalysisRepo Repository for email-based analyses
 * @property smsAnalysisRepo Repository for SMS-based analyses
 * @property fileAnalysisRepo Repository for file-based analyses
 * @property urlAnalysisRepo Repository for URL-based analyses
 */
@HiltViewModel
internal class ResultsScreenViewModel
    @Inject
    constructor(
        @EmailAnalysesRepository private val emailAnalysisRepo:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @SmsAnalysesRepository private val smsAnalysisRepo:
            AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val fileAnalysisRepo: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        private val urlAnalysisRepo: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>
    ) : ViewModel() {
        private val pageStats = 1
        private val pageSize = 10

        private val _uiState = MutableStateFlow<ResultsUiState>(ResultsUiState())
        val uiState = _uiState.asStateFlow()

        /**
         * Loads and aggregates analysis statistics from various repositories.
         *
         * Fetches paginated analysis results for emails, SMS messages, files, and URLs.
         * Processes the outcomes to calculate verdict statistics for each type.
         * Updates the UI state with the calculated statistics.
         */
        fun loadStats() {
            viewModelScope.launch {
                val emailOutcome = emailAnalysisRepo.getManyPaged(pageStats, pageSize)
                val smsOutcome = smsAnalysisRepo.getManyPaged(pageStats, pageSize)
                val fileOutcome = fileAnalysisRepo.getManyPaged(pageStats, pageSize)
                val urlOutcome = urlAnalysisRepo.getManyPaged(pageStats, pageSize)

                val emailVerdictStats =
                    when (emailOutcome) {
                        is Outcome.Success -> emailOutcome.value.messageCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }

                val smsVerdictStats =
                    when (smsOutcome) {
                        is Outcome.Success -> smsOutcome.value.messageCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }

                val fileVerdictStats =
                    when (fileOutcome) {
                        is Outcome.Success -> fileOutcome.value.multiCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }

                val urlVerdictStats =
                    when (urlOutcome) {
                        is Outcome.Success -> urlOutcome.value.multiCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }

                _uiState.value =
                    ResultsUiState(
                        emailAnalysesStats = emailVerdictStats,
                        smsAnalysesStats = smsVerdictStats,
                        fileAnalysesStats = fileVerdictStats,
                        urlAnalysesStats = urlVerdictStats
                    )
            }
        }

        /**
         * Counts verdict statistics for a list of message analyses.
         *
         * @return [VerdictStatsState] containing counts of different verdicts for the analyses
         */
        private fun List<MessageAnalysis>.messageCountVerdictStats(): VerdictStatsState =
            countVerdictStats(
                map {
                    it.verdict
                }
            )

        /**
         * Counts verdict statistics for a list of multi-analyses (URLs or files).
         *
         * @return [VerdictStatsState] containing counts of different final verdicts for the analyses
         */
        private fun List<MultiAnalysis>.multiCountVerdictStats(): VerdictStatsState =
            countVerdictStats(map { it.finalVerdict })

        /**
         * Calculates statistics for different verdict types from a list of verdicts.
         *
         * @param verdicts List of [Verdict] objects to analyze
         * @return [VerdictStatsState] containing counts for each verdict type:
         *         - cleanVerdicts: count of Undetected verdicts
         *         - suspiciousVerdicts: count of Suspicious verdicts
         *         - maliciousVerdicts: count of Malicious verdicts
         *         - unknownVerdicts: count of Unknown verdicts
         */
        private fun countVerdictStats(verdicts: List<Verdict>): VerdictStatsState =
            VerdictStatsState(
                cleanVerdicts = verdicts.count { it == Verdict.Undetected },
                suspiciousVerdicts = verdicts.count { it == Verdict.Suspicious },
                maliciousVerdicts = verdicts.count { it == Verdict.Malicious },
                unknownVerdicts = verdicts.count { it == Verdict.Unknown }
            )
    }