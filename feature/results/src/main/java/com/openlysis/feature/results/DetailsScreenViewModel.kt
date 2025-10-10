package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Abstract ViewModel for displaying details of an analysis.
 *
 * @param TModel The type of model being displayed, must extend [Model].
 * @property repository The repository used to fetch analysis data.
 * @property updateTracker Tracker for analysis updates
 * @param appScope The application-level coroutine scope.
 */
internal abstract class DetailsScreenViewModel<TModel : Model>(
    private val repository: AnalysesRepository<*, TModel>,
    private val updateTracker: AnalysisUpdateTracker<TModel>,
    private val appScope: CoroutineScope
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailsUiState<TModel>>(DetailsUiState.None)
    val uiState = _uiState.asStateFlow()

    private var analysisId: String = ""

    /**
     * Loads the analysis details for the given ID.
     *
     * @param id The ID of the analysis to load.
     */
    fun loadAnalysis(id: String) {
        analysisId = id
        _uiState.update { DetailsUiState.Loading }
        viewModelScope.launch {
            val outcome = repository.getById(id)
            when (outcome) {
                is Outcome.Success -> {
                    val analysis = outcome.value
                    val isRefreshable = analysis.isRefreshable()
                    if (isRefreshable) reactToTrackerEvents(analysis)
                    _uiState.update {
                        DetailsUiState.Success(
                            analysis,
                            isRefreshable,
                            isRefreshing = updateTracker.isTracking.value
                        )
                    }
                }
                is Outcome.Failure -> _uiState.update { DetailsUiState.Failure(outcome.error) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (analysisId.isEmpty()) return
        appScope.launch { updateTracker.untrack(analysisId) }
    }

    /**
     * Returns the current status of the analysis result.
     *
     * @param result The analysis model to evaluate.
     * @return The [AnalysisStatus] representing the current state of the analysis.
     */
    protected abstract fun getStatus(result: TModel): AnalysisStatus

    private suspend fun reactToTrackerEvents(analysis: TModel) {
        updateTracker.track(analysis.id)
        updateTracker.updates
            .filter { it.id == analysis.id }
            .onEach(::handleIncomingUpdate)
            .launchIn(viewModelScope)

        updateTracker.isTracking
            .onEach(::handleIsTrackingChange)
            .launchIn(viewModelScope)
    }

    private suspend fun handleIncomingUpdate(analysis: TModel) {
        repository.updateLocally(analysis)
        _uiState.update {
            DetailsUiState.Success(
                analysis,
                isRefreshable = analysis.isRefreshable(),
                isRefreshing = updateTracker.isTracking.value
            )
        }
    }

    private fun handleIsTrackingChange(isTracking: Boolean) {
        val detailsUiState = uiState.value
        if (detailsUiState !is DetailsUiState.Success) return

        _uiState.update {
            detailsUiState.copy(isRefreshing = isTracking)
        }
    }

    private fun TModel.isRefreshable(): Boolean {
        val status = getStatus(this)
        return status == AnalysisStatus.Queued || status == AnalysisStatus.InProgress
    }
}