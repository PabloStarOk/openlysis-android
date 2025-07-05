package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.results.components.VerdictStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the initial results screen.
 *
 * @property messageAnalysisRepo Repository for message-based analyses
 * @property fileAnalysisRepo Repository for file-based analyses
 * @property urlAnalysisRepo Repository for URL-based analyses
 */
@HiltViewModel
internal class ResultsScreenViewModel
    @Inject
    constructor(
        private val messageAnalysisRepo: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        private val fileAnalysisRepo: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        private val urlAnalysisRepo: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>
    ) : ViewModel() {
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
                val outcome = messageAnalysisRepo.getManyPaged(1)
                _emailAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.countVerdictStats(MessageType.Email)
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Loads and updates SMS verdict statistics.
         */
        fun loadSmsVerdictStats() {
            viewModelScope.launch {
                val outcome = messageAnalysisRepo.getManyPaged(1)
                _smsAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.countVerdictStats(MessageType.Sms)
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Loads and updates file verdict statistics.
         */
        fun loadFileVerdictStats() {
            viewModelScope.launch {
                val outcome = fileAnalysisRepo.getManyPaged(1)
                _fileAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.countVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Loads and updates URL verdict statistics.
         */
        fun loadUrlVerdictStats() {
            viewModelScope.launch {
                val outcome = urlAnalysisRepo.getManyPaged(1)
                _urlAnalysisStats.value =
                    when (outcome) {
                        is Outcome.Success -> outcome.value.countVerdictStats()
                        is Outcome.Failure -> VerdictStatsState.Zero
                    }
            }
        }

        /**
         * Counts verdict statistics for a list of message analyses filtered by message type.
         *
         * @param messageType The type of message to filter analyses by
         * @return [VerdictStatsState] containing counts of different verdicts for the filtered analyses
         */
        private fun List<MessageAnalysis>.countVerdictStats(
            messageType: MessageType
        ): VerdictStatsState {
            val analyses =
                this.filter {
                    it.message.type == messageType
                }
            return VerdictStatsState(
                cleanVerdicts =
                    analyses.count {
                        it.verdict == Verdict.Undetected
                    },
                suspiciousVerdicts =
                    analyses.count { it.verdict == Verdict.Suspicious },
                maliciousVerdicts =
                    analyses.count {
                        it.verdict ==
                            Verdict.Malicious
                    },
                unknownVerdicts =
                    analyses.count {
                        it.verdict == Verdict.Unknown
                    }
            )
        }

        /**
         * Counts verdict statistics for a list of multi-analyses (URLs or files).
         *
         * @return [VerdictStatsState] containing counts of different final verdicts for the analyses
         */
        private fun List<MultiAnalysis>.countVerdictStats(): VerdictStatsState =
            VerdictStatsState(
                cleanVerdicts =
                    this.count {
                        it.finalVerdict == Verdict.Undetected
                    },
                suspiciousVerdicts =
                    this.count { it.finalVerdict == Verdict.Suspicious },
                maliciousVerdicts =
                    this.count {
                        it.finalVerdict ==
                            Verdict.Malicious
                    },
                unknownVerdicts =
                    this.count {
                        it.finalVerdict == Verdict.Unknown
                    }
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