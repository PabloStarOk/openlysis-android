package com.openlysis.feature.results

import com.openlysis.core.outcome.AppError

/**
 * Represents the loading state for results data fetching.
 */
internal sealed interface LoadingState {
    /**
     * Indicates that data loading is currently in progress.
     */
    data object InProgress : LoadingState

    /**
     * Indicates that no data loading operation is currently active.
     */
    data object Idle : LoadingState

    /**
     * Indicates an error state during data loading.
     *
     * @property error The error that occurred during the loading operation.
     */
    data class Error(
        val error: AppError
    ) : LoadingState
}