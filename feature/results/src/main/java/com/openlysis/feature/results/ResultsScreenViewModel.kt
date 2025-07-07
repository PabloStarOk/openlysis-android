package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.analysis.core.di.EmailAnalysesRepository
import com.openlysis.data.analysis.core.di.SmsAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.AnalyzeUrl
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.MultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.Outcome
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.results.components.VerdictStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.count

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
        private val pageSize = 20

        private val _emailAnalysisStats =
            MutableStateFlow<VerdictStatsState>(VerdictStatsState.Zero)
        private val _smsAnalysisStats = MutableStateFlow<VerdictStatsState>(VerdictStatsState.Zero)
        private val _fileAnalysisStats = MutableStateFlow<VerdictStatsState>(VerdictStatsState.Zero)
        private val _urlAnalysisStats = MutableStateFlow<VerdictStatsState>(VerdictStatsState.Zero)

        val emailAnalysisStats: StateFlow<VerdictStatsState> = _emailAnalysisStats.stateInDefault()
        val smsAnalysisStats: StateFlow<VerdictStatsState> = _smsAnalysisStats.stateInDefault()
        val fileAnalysisStats: StateFlow<VerdictStatsState> = _fileAnalysisStats.stateInDefault()
        val urlAnalysisStats: StateFlow<VerdictStatsState> = _urlAnalysisStats.stateInDefault()

        /**
         * Loads and updates email verdict statistics.
         */
        fun loadEmailVerdictStats() {
            viewModelScope.launch {
                val outcome = emailAnalysisRepo.getManyPaged(pageStats, pageSize)
                _emailAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.messageCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Loads and updates SMS verdict statistics.
         */
        fun loadSmsVerdictStats() {
            viewModelScope.launch {
                val outcome = smsAnalysisRepo.getManyPaged(pageStats, pageSize)
                _smsAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.messageCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Loads and updates file verdict statistics.
         */
        fun loadFileVerdictStats() {
            viewModelScope.launch {
                val outcome = fileAnalysisRepo.getManyPaged(pageStats, pageSize)
                _fileAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.multiCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Loads and updates URL verdict statistics.
         */
        fun loadUrlVerdictStats() {
            viewModelScope.launch {
                val outcome = urlAnalysisRepo.getManyPaged(pageStats, pageSize)
                _urlAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.multiCountVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
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

        /**
         * Extension function to convert a [MutableStateFlow] to a [StateFlow] with default configuration.
         *
         * @return [StateFlow] configured with WhileSubscribed sharing policy and zero initial value
         */
        private fun MutableStateFlow<VerdictStatsState>.stateInDefault() =
            this.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = VerdictStatsState.Zero
            )
    }