package com.openlysis.feature.results

import androidx.compose.runtime.Immutable
import com.openlysis.feature.results.components.VerdictStatsState

/**
 * UI state for main results screen.
 *
 * @param emailAnalysesStats Statistics of email analysis verdicts
 * @param smsAnalysesStats Statistics of SMS analysis verdicts
 * @param fileAnalysesStats Statistics of file analysis verdicts
 * @param urlAnalysesStats Statistics of URL analysis verdicts
 * @param emailAnalysesInProgress Number of email analyses currently in progress
 * @param smsAnalysesInProgress Number of SMS analyses currently in progress
 * @param fileAnalysesInProgress Number of file analyses currently in progress
 * @param urlAnalysesInProgress Number of URL analyses currently in progress
 */
@Immutable
internal data class ResultsUiState(
    val emailAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val smsAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val fileAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val urlAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val emailAnalysesInProgress: Int = 0,
    val smsAnalysesInProgress: Int = 0,
    val fileAnalysesInProgress: Int = 0,
    val urlAnalysesInProgress: Int = 0
)