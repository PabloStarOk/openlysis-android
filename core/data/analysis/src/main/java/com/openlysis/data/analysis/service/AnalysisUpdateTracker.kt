package com.openlysis.data.analysis.service

import com.openlysis.data.analysis.model.common.Model
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Tracks updates for a set of analysis models.
 *
 * @param TAnalysis The type of analysis model being tracked.
 */
interface AnalysisUpdateTracker<TAnalysis : Model> {
    /**
     * A flow emitting updates for tracked analysis models.
     */
    val updates: SharedFlow<TAnalysis>

    /**
     * A flow emitting a boolean indicating whether tracking is active.
     */
    val isTracking: StateFlow<Boolean>

    /**
     * Starts tracking updates for the specified analysis IDs.
     *
     * @param analysisIds The IDs of the analyses to track.
     */
    suspend fun track(vararg analysisIds: String)

    /**
     * Stops tracking updates for the specified analysis IDs.
     *
     * @param analysisIds The IDs of the analyses to untrack.
     */
    suspend fun untrack(vararg analysisIds: String)
}