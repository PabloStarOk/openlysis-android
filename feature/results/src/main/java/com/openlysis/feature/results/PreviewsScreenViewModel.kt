package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Outcome
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.components.AnalysisPreviewState
import com.openlysis.feature.results.components.VerdictStatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

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

    private val _uiState =
        MutableStateFlow<PreviewsUiState>(PreviewsUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Loads a paginated batch of analysis previews.
     */
    fun loadPreviews() {
        _uiState.update { it.copy(loadingState = LoadingState.InProgress) }
        val existingAnalysisIds = _uiState.value.previews.map { it.id }

        viewModelScope.launch {
            val outcome = repository.getManyPaged(nextPage, pageSize)
            val newResults =
                when (outcome) {
                    is Outcome.Success ->
                        outcome.value.filterNot {
                            it.id in existingAnalysisIds
                        }
                    is Outcome.Failure -> {
                        _uiState.update {
                            it.copy(
                                loadingState = LoadingState.Error(outcome.error)
                            )
                        }
                        return@launch
                    }
                }

            val newState =
                _uiState.updateAndGet {
                    val previews = it.previews + newResults.map { convertToPreview(it) }
                    PreviewsUiState(
                        previews = previews,
                        verdictStats = calculateVerdictStats(previews),
                        canLoadMore = newResults.size >= pageSize,
                        loadingState = LoadingState.Idle
                    )
                }
            if (newState.canLoadMore) nextPage++
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
        viewModelScope
            .launch {
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