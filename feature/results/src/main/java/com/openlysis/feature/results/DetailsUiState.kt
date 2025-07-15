package com.openlysis.feature.results

import com.openlysis.data.analysis.model.common.AnalysisError
import com.openlysis.data.analysis.model.common.Model

/**
 * Represents the UI state for details screen.
 *
 * @param TAnalysis The type of analysis model.
 */
internal sealed interface DetailsUiState<out TAnalysis : Model> {
    /**
     * UI state representing a successful analysis result.
     *
     * @property analysis The analysis data.
     */
    data class Success<TAnalysis : Model>(
        val analysis: TAnalysis
    ) : DetailsUiState<TAnalysis>

    /**
     * UI state representing a failure with an error.
     *
     * @property error The error encountered during analysis fetching.
     */
    data class Failure(
        val error: AnalysisError
    ) : DetailsUiState<Nothing>

    /**
     * UI state representing a loading state.
     */
    data object Loading : DetailsUiState<Nothing>

    /**
     * UI state representing no data or initial state.
     */
    data object None : DetailsUiState<Nothing>
}