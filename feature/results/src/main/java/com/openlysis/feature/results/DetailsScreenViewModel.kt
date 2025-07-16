package com.openlysis.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Outcome
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Abstract ViewModel for displaying details of an analysis.
 *
 * @param TModel The type of model being displayed, must extend [Model].
 * @property repository The repository used to fetch analysis data.
 */
internal abstract class DetailsScreenViewModel<TModel : Model>(
    val repository: AnalysesRepository<*, TModel>
) : ViewModel() {
    private var pollingJob: Job? = null
    private var isResultFinal = false
    private val _uiState = MutableStateFlow<DetailsUiState<TModel>>(DetailsUiState.None)
    private val _isPolling = MutableStateFlow<Boolean>(false)
    val uiState = _uiState.asStateFlow()
    val isPolling = _isPolling.asStateFlow()

    /**
     * Loads the analysis details for the given ID.
     *
     * @param id The ID of the analysis to load.
     */
    fun loadAnalysis(id: String) {
        viewModelScope.launch {
            _uiState.update {
                DetailsUiState.Loading
            }

            val outcome = repository.getById(id)
            when (outcome) {
                is Outcome.Success ->
                    _uiState.update {
                        DetailsUiState.Success(
                            outcome.value
                        )
                    }
                is Outcome.Failure ->
                    _uiState.update {
                        DetailsUiState.Failure(
                            outcome.error
                        )
                    }
            }
        }
    }

    /**
     * Starts polling for analysis updates by ID.
     *
     * @param id The ID of the analysis to poll for updates.
     */
    fun startPolling(id: String) {
        if (pollingJob != null || isResultFinal) return

        pollingJob =
            viewModelScope.launch {
                while (isActive && !isResultFinal) {
                    if (!isPolling.value) {
                        _isPolling.update { true }
                    }
                    val outcome = repository.getUpdatedById(id)
                    if (outcome !is Outcome.Success) {
                        continue
                    }

                    _uiState.update { DetailsUiState.Success(outcome.value) }
                    val status = getStatus(outcome.value)
                    isResultFinal = status != AnalysisStatus.Queued &&
                        status != AnalysisStatus.InProgress
                    if (isResultFinal) {
                        stopPolling()
                        break
                    }

                    delay(3_000L)
                }
            }
    }

    /**
     * Stops the polling job if it is running and updates the polling state.
     */
    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
        _isPolling.update { false }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }

    /**
     * Returns the current status of the analysis result.
     *
     * @param result The analysis model to evaluate.
     * @return The [AnalysisStatus] representing the current state of the analysis.
     */
    protected abstract fun getStatus(result: TModel): AnalysisStatus
}