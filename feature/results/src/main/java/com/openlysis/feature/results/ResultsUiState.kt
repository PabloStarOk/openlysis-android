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
 */
@Immutable
internal data class ResultsUiState(
    val emailAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val smsAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val fileAnalysesStats: VerdictStatsState = VerdictStatsState.Zero,
    val urlAnalysesStats: VerdictStatsState = VerdictStatsState.Zero
)