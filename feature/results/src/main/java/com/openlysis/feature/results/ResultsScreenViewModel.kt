package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.di.MessageAnalysisDependency
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.MultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
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
        @MessageAnalysisDependency(MessageType.Email)
        private val emailAnalysisRepo: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @MessageAnalysisDependency(MessageType.Sms)
        private val smsAnalysisRepo: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
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

                var emailVerdictStats: VerdictStatsState = VerdictStatsState.Zero
                var emailAnalysesInProgress = 0
                if (emailOutcome is Outcome.Success) {
                    emailVerdictStats = emailOutcome.value.getMessageAnalysesVerdictStats()
                    emailAnalysesInProgress = emailOutcome.value.countMessageAnalysesInProgress()
                }

                var smsVerdictStats: VerdictStatsState = VerdictStatsState.Zero
                var smsAnalysesInProgress = 0
                if (smsOutcome is Outcome.Success) {
                    smsVerdictStats = smsOutcome.value.getMessageAnalysesVerdictStats()
                    smsAnalysesInProgress = smsOutcome.value.countMessageAnalysesInProgress()
                }

                var fileVerdictStats: VerdictStatsState = VerdictStatsState.Zero
                var fileAnalysesInProgress = 0
                if (fileOutcome is Outcome.Success) {
                    fileVerdictStats = fileOutcome.value.getMultiAnalysesVerdictStats()
                    fileAnalysesInProgress = fileOutcome.value.countMultiAnalysesInProgress()
                }

                var urlVerdictStats: VerdictStatsState = VerdictStatsState.Zero
                var urlAnalysesInProgress = 0
                if (urlOutcome is Outcome.Success) {
                    urlVerdictStats = urlOutcome.value.getMultiAnalysesVerdictStats()
                    urlAnalysesInProgress = urlOutcome.value.countMultiAnalysesInProgress()
                }

                _uiState.value =
                    ResultsUiState(
                        emailAnalysesStats = emailVerdictStats,
                        smsAnalysesStats = smsVerdictStats,
                        fileAnalysesStats = fileVerdictStats,
                        urlAnalysesStats = urlVerdictStats,
                        emailAnalysesInProgress = emailAnalysesInProgress,
                        smsAnalysesInProgress = smsAnalysesInProgress,
                        fileAnalysesInProgress = fileAnalysesInProgress,
                        urlAnalysesInProgress = urlAnalysesInProgress
                    )
            }
        }

        /**
         * Counts verdict statistics for a list of message analyses.
         *
         * @return [VerdictStatsState] containing counts of different verdicts for the analyses
         */
        private fun List<MessageAnalysis>.getMessageAnalysesVerdictStats(): VerdictStatsState =
            countVerdictStats(
                map {
                    it.verdict
                }
            )

        /**
         * Counts the number of message analyses that are either queued or in progress.
         *
         * @receiver List of [MessageAnalysis] objects to check.
         * @return The count of analyses with status Queued or InProgress.
         */
        private fun List<MessageAnalysis>.countMessageAnalysesInProgress(): Int =
            count { it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress }

        /**
         * Counts verdict statistics for a list of multi-analyses (URLs or files).
         *
         * @return [VerdictStatsState] containing counts of different final verdicts for the analyses
         */
        private fun List<MultiAnalysis>.getMultiAnalysesVerdictStats(): VerdictStatsState =
            countVerdictStats(map { it.finalVerdict })

        /**
         * Counts the number of multi-analyses (e.g., files or URLs) that are either queued or in progress.
         *
         * @receiver List of [MultiAnalysis] objects to check.
         * @return The count of analyses with status Queued or InProgress.
         */
        private fun List<MultiAnalysis>.countMultiAnalysesInProgress(): Int =
            count { it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress }

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