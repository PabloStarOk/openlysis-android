package com.openlysis.feature.results

import androidx.compose.runtime.Immutable
import com.openlysis.feature.results.components.AnalysisPreviewState
import com.openlysis.feature.results.components.VerdictStatsState
import com.openlysis.feature.results.components.filter.FiltersState

/**
 * UI state for previews screen.
 *
 * @property previews List of analysis preview states to be displayed
 * @property verdictStats Statistics about analysis verdicts to be displayed
 * @property canLoadMore Indicates if more previews can be loaded
 * @property loadingState Current [LoadingState] of the previews
 */
@Immutable
internal data class PreviewsUiState(
    val previews: List<AnalysisPreviewState> = emptyList(),
    val verdictStats: VerdictStatsState = VerdictStatsState.Companion.Zero,
    val canLoadMore: Boolean = true,
    val loadingState: LoadingState = LoadingState.Idle,
    val filtersState: FiltersState
)