package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import com.openlysis.feature.results.components.VerdictStatsState
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import com.openlysis.feature.results.components.preview.FiltersState
import com.openlysis.feature.results.components.preview.SortableField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.ConcurrentHashMap

/**
 * Abstract base ViewModel for managing paginated previews of analysis results.
 *
 * @param TResult The type of analysis result model, must extend [Model]
 * @property repository Repository for accessing and managing analysis data
 * @property updateTracker Tracker for analysis updates
 * @property appScope Application-level coroutine scope
 */
internal abstract class PreviewsScreenViewModel<TResult : Model>(
    private val repository: AnalysesRepository<*, TResult>,
    private val updateTracker: AnalysisUpdateTracker<TResult>,
    private val appScope: CoroutineScope
) : ViewModel() {
    private var nextPage = 1
    private val pageSize = 10
    private val loadedPreviews = ConcurrentHashMap<String, AnalysisPreviewState>()

    val defaultFiltersState = FiltersState.Default

    private val _uiState =
        MutableStateFlow<PreviewsUiState>(
            PreviewsUiState(
                filtersState = defaultFiltersState
            )
        )
    val uiState =
        _uiState
            .onStart {
                startReactingToTrackerUpdates()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PreviewsUiState(filtersState = defaultFiltersState)
            )

    /**
     * Loads a paginated batch of analysis previews.
     */
    fun loadPreviews() {
        _uiState.update { it.copy(loadingState = LoadingState.InProgress) }
        viewModelScope.launch {
            val outcome = repository.getManyPaged(nextPage, pageSize)
            val newResults =
                when (outcome) {
                    is Outcome.Success -> outcome.value
                    is Outcome.Failure -> {
                        _uiState.update {
                            it.copy(
                                loadingState = LoadingState.Error(outcome.error)
                            )
                        }
                        return@launch
                    }
                }

            val existingPreviewsIds = loadedPreviews.keys
            val newUniquePreviews =
                newResults
                    .filterNot { it.id in existingPreviewsIds }
                    .map { convertToPreview(it) }
                    .associateBy { it.id }
            loadedPreviews.putAll(newUniquePreviews)

            _uiState.update { currentState ->
                val filteredPreviews = filterLoadedPreviews(currentState.filtersState)
                currentState.copy(
                    previews = filteredPreviews.toList(),
                    verdictStats = calculateVerdictStats(filteredPreviews),
                    canLoadMore = newResults.size >= pageSize,
                    loadingState = LoadingState.Idle
                )
            }

            if (uiState.value.canLoadMore) nextPage++
            trackUpdatablePreviews(newUniquePreviews.values)
        }
    }

    /**
     * Applies filters with the loaded previews and update the UI state.
     *
     * @param newFiltersState The new filter configuration to be applied
     */
    fun updateFilters(newFiltersState: FiltersState) {
        val filteredPreviews = filterLoadedPreviews(newFiltersState)
        _uiState.update {
            it.copy(
                previews = filteredPreviews.toList(),
                verdictStats = calculateVerdictStats(filteredPreviews),
                filtersState = newFiltersState
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        untrackUpdatablePreviews()
    }

    /**
     * Converts a result model into an analysis preview state.
     *
     * @param result The analysis result model to convert
     * @return The converted [AnalysisPreviewState]
     */
    protected abstract fun handlePreviewConversion(result: TResult): AnalysisPreviewState

    /**
     * Converts a result model to an [AnalysisPreviewState], setting the isRefreshing flag
     * if the preview is updatable and the tracker is currently tracking.
     *
     * @param result The analysis result model to convert.
     * @return The corresponding [AnalysisPreviewState].
     */
    private fun convertToPreview(result: TResult): AnalysisPreviewState =
        handlePreviewConversion(result).let {
            it.copy(isRefreshing = it.isRefreshable() && updateTracker.isTracking.value)
        }

    /**
     * Applies filtering and sorting operations to [loadedPreviews] based on the specified state filters.
     *
     * @param filtersState The filters to be applied to the [loadedPreviews].
     * @return A filtered and sorted set of analysis preview states that match the filter criteria
     */
    private fun filterLoadedPreviews(filtersState: FiltersState): Set<AnalysisPreviewState> {
        val startDate =
            Instant
                .fromEpochMilliseconds(filtersState.startDateMillis)
                .toLocalDateTime(TimeZone.UTC)
                .date

        val endDate =
            Instant
                .fromEpochMilliseconds(filtersState.endDateMillis)
                .toLocalDateTime(TimeZone.UTC)
                .date

        var filteredPreviews =
            loadedPreviews.values
                .filter {
                    val analysisDate =
                        it.startedDate
                            .toKotlinInstant()
                            .toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).date
                    analysisDate >= startDate && analysisDate <= endDate
                }.filter { it.verdict in filtersState.selectedVerdicts }
                .filter { it.status in filtersState.selectedStatuses }

        filtersState.sortingFields.forEach { filteredPreviews = sortPreviews(filteredPreviews, it) }
        return filteredPreviews.toSet()
    }

    /**
     * Sorts a list of analysis preview states based on the specified sort field.
     *
     * @param previews The list of analysis preview states to be sorted
     * @param sortBy The field by which the previews should be sorted
     * @return A new sorted list of analysis preview states
     */
    private fun sortPreviews(
        previews: List<AnalysisPreviewState>,
        sortBy: SortableField
    ): List<AnalysisPreviewState> =
        when (sortBy) {
            SortableField.Date -> previews.sortedByDescending { it.startedDate }
            SortableField.Verdict -> previews.sortedByDescending { it.verdict }
            SortableField.Status -> previews.sortedByDescending { it.status }
        }

    /**
     * Calculates statistics for different verdict types from a collection of analysis previews.
     *
     * @param previews Collection of analysis preview states to analyze
     * @return [VerdictStatsState] containing counts for each verdict type
     */
    private fun calculateVerdictStats(
        previews: Collection<AnalysisPreviewState>
    ): VerdictStatsState =
        VerdictStatsState(
            cleanVerdicts = previews.count { it.verdict == Verdict.Undetected },
            suspiciousVerdicts = previews.count { it.verdict == Verdict.Suspicious },
            maliciousVerdicts = previews.count { it.verdict == Verdict.Malicious },
            unknownVerdicts = previews.count { it.verdict == Verdict.Unknown }
        )

    /**
     * Subscribes to analysis update events from the update tracker and handles them.
     */
    private fun startReactingToTrackerUpdates() {
        updateTracker.updates.onEach(::handleAnalysisUpdate).launchIn(viewModelScope)
        updateTracker.isTracking.onEach(::handleIsTrackingChange).launchIn(viewModelScope)
    }

    /**
     * Tracks previews that are updatable (Queued or InProgress) using the update tracker.
     *
     * @param previews List of [AnalysisPreviewState] to check and track if updatable.
     */
    private suspend fun trackUpdatablePreviews(previews: Collection<AnalysisPreviewState>) {
        val updatablePreviewsIds = previews.filter { it.isRefreshable() }.map { it.id }
        updateTracker.track(*updatablePreviewsIds.toTypedArray())
    }

    /**
     * Remove all tracked previews that are currently updatable (Queued or InProgress) using the update tracker.
     * This is typically called when the ViewModel is being cleared to ensure no unnecessary tracking remains.
     */
    private fun untrackUpdatablePreviews() {
        var updatablePreviewsIds = loadedPreviews.values.filter { it.isRefreshable() }.map { it.id }
        appScope.launch {
            updateTracker.untrack(*updatablePreviewsIds.toTypedArray())
        }
    }

    /**
     * Handles an incoming analysis update by updating the local repository and UI state.
     *
     * @param updatedAnalysis The updated analysis result to process.
     */
    private suspend fun handleAnalysisUpdate(updatedAnalysis: TResult) {
        repository.updateLocally(updatedAnalysis)
        val updatedPreview = convertToPreview(updatedAnalysis)
        loadedPreviews[updatedPreview.id] = updatedPreview

        _uiState.update { currentState ->
            val filteredPreviews = filterLoadedPreviews(currentState.filtersState)
            currentState.copy(
                previews = filteredPreviews.toList(),
                verdictStats = calculateVerdictStats(filteredPreviews)
            )
        }
    }

    /**
     * Updates the refreshing state of all refreshable previews when the tracking status changes.
     *
     * @param isTracking Indicates whether tracking is currently active.
     */
    private fun handleIsTrackingChange(isTracking: Boolean) {
        val updatedPreviews =
            loadedPreviews.values
                .filter { it.isRefreshable() }
                .map { it.copy(isRefreshing = isTracking) }
                .associateBy { it.id }
        loadedPreviews.putAll(updatedPreviews)

        _uiState.update { currentState ->
            val updatedUiPreviews =
                currentState.previews.map {
                    it.copy(
                        isRefreshing = it.isRefreshable() && isTracking
                    )
                }
            currentState.copy(previews = updatedUiPreviews)
        }
    }
}