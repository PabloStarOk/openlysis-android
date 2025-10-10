package com.openlysis.feature.tools.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Abstract ViewModel for managing the analysis tool screens.
 * Handles analysis request lifecycle and enforces validation and cancellation logic.
 */
internal abstract class AnalysisToolScreenViewModel : ViewModel() {
    private var requestJob: Job? = null

    /**
     * Starts the analysis.
     */
    fun startAnalysis() {
        requestJob = viewModelScope.launch { handleStartAnalysis() }
        requestJob?.invokeOnCompletion { requestJob = null }
    }

    /**
     * Cancels the current analysis request and triggers cancellation logic.
     */
    fun cancelRequest() {
        requestJob?.cancel()
        onCancelRequest()
    }

    /**
     * Handles the logic for starting the analysis.
     */
    protected abstract suspend fun handleStartAnalysis()

    /**
     * Executed when the analysis request is cancelled.
     */
    protected abstract fun onCancelRequest()
}