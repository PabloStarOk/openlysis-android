package com.openlysis.feature.results.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Outcome
import com.openlysis.feature.results.DetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private val _uiState = MutableStateFlow<DetailsUiState<TModel>>(DetailsUiState.None)
    val uiState = _uiState.asStateFlow()

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
}