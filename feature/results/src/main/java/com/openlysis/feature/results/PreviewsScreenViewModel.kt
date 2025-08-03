package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.feature.results.components.VerdictStatsState
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import com.openlysis.feature.results.components.preview.FiltersState
import com.openlysis.feature.results.components.preview.SortableField
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
 */
internal abstract class PreviewsScreenViewModel<TResult : Model>(
    private val repository: AnalysesRepository<*, TResult>
) : ViewModel() {
    private var nextPage = 1
    private val pageSize = 10
    private val loadedPreviews = ConcurrentHashMap.newKeySet<AnalysisPreviewState>()

    val defaultFiltersState = FiltersState.Default

    private val _uiState =
        MutableStateFlow<PreviewsUiState>(
            PreviewsUiState(
                filtersState = defaultFiltersState
            )
        )
    val uiState = _uiState.asStateFlow()

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

            _uiState.update { currentState ->
                val existingPreviewsIds = loadedPreviews.map { it.id }
                val newUniquePreviews =
                    newResults
                        .filterNot { it.id in existingPreviewsIds }
                        .map { convertToPreview(it) }

                loadedPreviews.addAll(newUniquePreviews)
                val filteredPreviews = filterLoadedPreviews(currentState.filtersState)
                currentState.copy(
                    previews = filteredPreviews.toList(),
                    verdictStats = calculateVerdictStats(filteredPreviews),
                    canLoadMore = newResults.size >= pageSize,
                    loadingState = LoadingState.Idle
                )
            }

            if (uiState.value.canLoadMore) nextPage++
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

    /**
     * Refreshes a specific analysis preview by fetching updated data from the repository.
     *
     * @param id The unique identifier of the preview to refresh
     * @param onFinished A suspend function to be called after the refresh operation completes,
     *                  regardless of success or failure
     */
    fun refreshPreview(
        id: String,
        onFinished: suspend () -> Unit
    ) {
        viewModelScope.launch {
            val outcome = repository.getUpdatedById(id)
            if (outcome is Outcome.Success) {
                _uiState.update {
                    val previewsMap = it.previews.associateBy { it.id }.toMutableMap()
                    previewsMap[id] = convertToPreview(outcome.value)
                    it.copy(
                        previews = previewsMap.values.toList(),
                        verdictStats = calculateVerdictStats(previewsMap.values)
                    )
                }
            }
            onFinished()
        }
    }

    /**
     * Refreshes all preview states that are either queued or in progress by fetching their
     * updated data from the repository. Updates the UI state with any new information received.
     *
     * @param onFinished A suspend function to be executed after all previews have been refreshed,
     *                  regardless of the operation's success or failure
     */
    fun refreshAllPreviews(onFinished: suspend () -> Unit) {
        val refreshablePreviews =
            uiState.value.previews
                .filter {
                    it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress
                }.associateBy { it.id }

        viewModelScope.launch {
            if (refreshablePreviews.isEmpty()) {
                onFinished()
                return@launch
            }

            val deferredUpdates =
                refreshablePreviews.values
                    .map {
                        viewModelScope.async {
                            repository.getUpdatedById(it.id)
                        }
                    }.toTypedArray()

            val outcomes = awaitAll(*deferredUpdates)
            val newPreviews =
                outcomes
                    .filter { it is Outcome.Success }
                    .map { it as Outcome.Success }
                    .map { convertToPreview(it.value) }
                    .filterNot { refreshablePreviews[it.id] == it }

            _uiState.update {
                val previewsMap = it.previews.associateBy { it.id }.toMutableMap()
                newPreviews.forEach { previewsMap[it.id] = it }
                it.copy(
                    previews = previewsMap.values.toList(),
                    verdictStats = calculateVerdictStats(previewsMap.values)
                )
            }

            onFinished()
        }
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
            loadedPreviews
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
     * Converts a result model into an analysis preview state.
     *
     * @param result The analysis result model to convert
     * @return The converted [AnalysisPreviewState]
     */
    protected abstract fun convertToPreview(result: TResult): AnalysisPreviewState

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
}