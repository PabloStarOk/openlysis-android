package com.openlysis.feature.results.components.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict

/**
 * Internal state holder class for managing filter dialog state.
 *
 * @property initialState The initial state of filters to start with
 * @property defaultState The default state used for resetting filters
 */
internal class FiltersDialogState(
    initialState: FiltersState,
    private val defaultState: FiltersState
) {
    var filters by mutableStateOf(initialState)
        private set

    /**
     * Adds a new sorting field to the filters.
     *
     * @param field The sorting field to be added to the current filters
     */
    fun addSortingField(field: SortableField) {
        filters =
            filters.copy(
                sortingFields = filters.sortingFields + field
            )
    }

    /**
     * Removes a sorting field from the filters.
     *
     * @param field The sorting field to be removed from the current filters
     */
    fun removeSortingField(field: SortableField) {
        filters =
            filters.copy(
                sortingFields = filters.sortingFields - field
            )
    }

    /**
     * Adds a verdict to the selected verdicts filter.
     *
     * @param verdict The verdict to be added to the selection
     */
    fun addSelectedVerdict(verdict: Verdict) {
        filters =
            filters.copy(
                selectedVerdicts = filters.selectedVerdicts + verdict
            )
    }

    /**
     * Removes a verdict from the selected verdicts filter.
     *
     * @param verdict The verdict to be removed from the selection
     */
    fun removeSelectedVerdict(verdict: Verdict) {
        filters =
            filters.copy(
                selectedVerdicts = filters.selectedVerdicts - verdict
            )
    }

    /**
     * Adds an analysis status to the selected statuses filter.
     *
     * @param status The analysis status to be added to the selection
     */
    fun addSelectedStatus(status: AnalysisStatus) {
        filters =
            filters.copy(
                selectedStatuses = filters.selectedStatuses + status
            )
    }

    /**
     * Removes an analysis status from the selected statuses filter.
     *
     * @param status The analysis status to be removed from the selection
     */
    fun removeSelectedStatus(status: AnalysisStatus) {
        filters =
            filters.copy(
                selectedStatuses = filters.selectedStatuses - status
            )
    }

    /**
     * Updates the date range filter with new start and end dates.
     *
     * @param startDateMillis The start date in milliseconds
     * @param endDateMillis The end date in milliseconds
     */
    fun updateDateRange(
        startDateMillis: Long?,
        endDateMillis: Long?
    ) {
        if (startDateMillis != null && endDateMillis != null) {
            filters =
                filters.copy(
                    startDateMillis = startDateMillis,
                    endDateMillis = endDateMillis
                )
        }
    }

    /**
     * Resets the sorting order to the default state.
     */
    fun resetSortOrder() {
        filters =
            filters.copy(
                sortingFields = defaultState.sortingFields
            )
    }

    /**
     * Resets the selected verdicts to the default state.
     */
    fun resetSelectedVerdicts() {
        filters =
            filters.copy(
                selectedVerdicts = defaultState.selectedVerdicts
            )
    }

    /**
     * Resets the selected statuses to the default state.
     */
    fun resetSelectedStatuses() {
        filters =
            filters.copy(
                selectedStatuses = defaultState.selectedStatuses
            )
    }

    /**
     * Resets the date range to the default state.
     */
    fun resetDateRange() {
        filters =
            filters.copy(
                startDateMillis = defaultState.startDateMillis,
                endDateMillis = defaultState.endDateMillis
            )
    }

    /**
     * Resets all filters to their default state, including sorting fields,
     * verdicts, statuses, and date range.
     */
    fun resetAll() {
        filters =
            FiltersState(
                sortingFields = defaultState.sortingFields,
                selectedVerdicts = defaultState.selectedVerdicts,
                selectedStatuses = defaultState.selectedStatuses,
                startDateMillis = defaultState.startDateMillis,
                endDateMillis = defaultState.endDateMillis
            )
    }
}