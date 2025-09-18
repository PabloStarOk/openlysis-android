package com.openlysis.data.remote.signalr

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import com.openlysis.data.remote.signalr.dto.BaseAnalysisDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * Tracks updates for analyses using SignalR.
 *
 * @param TAnalysis The type of analysis model.
 * @param TDto The type of DTO sent over SignalR as updates.
 * @property appScope Coroutine scope for launching update jobs.
 * @property connectionProvider Provides SignalR hub connections.
 * @property dtoClass The [KClass] of the DTO for deserialization.
 * @property hubMethod The SignalR hub method to listen for updates.
 * @property repository Repository for accessing analysis.
 * @property isAnalysisUpdatableCallback Callback to determine if an analysis is updatable.
 */
internal class SignalRAnalysisUpdateTracker<
    TAnalysis : Model,
    TDto : BaseAnalysisDto<TAnalysis>
>(
    @ApplicationScope private val appScope: CoroutineScope,
    private val connectionProvider: SignalRConnectionProvider,
    private val dtoClass: KClass<TDto>,
    private val hubMethod: SignalRHubMethod,
    private val repository: AnalysesRepository<*, TAnalysis>,
    private val isAnalysisUpdatableCallback: (TAnalysis) -> Boolean
) : AnalysisUpdateTracker<TAnalysis> {
    private val trackedAnalyses = ConcurrentHashMap<String, Int>()
    private val _updates = MutableSharedFlow<TAnalysis>(extraBufferCapacity = 32, replay = 16)
    override val updates: SharedFlow<TAnalysis> = _updates.asSharedFlow()
    override val isTracking: StateFlow<Boolean> = connectionProvider.isAvailable

    override suspend fun track(vararg analysisIds: String) {
        if (analysisIds.isEmpty()) return

        analysisIds.forEach { id ->
            val existing = trackedAnalyses.getOrDefault(id, 0)
            trackedAnalyses[id] = existing + 1
        }
        connectionProvider.connect<TDto>(hubMethod, this::handleIncomingUpdate, dtoClass)
        untrackNonUpdatable(*analysisIds)
    }

    override suspend fun untrack(vararg analysisIds: String) {
        if (analysisIds.isEmpty()) return

        analysisIds.forEach { id ->
            val existing = trackedAnalyses[id]
            if (existing == null) return@forEach
            if (existing == 1) trackedAnalyses.remove(id) else trackedAnalyses[id] = existing - 1
        }
        disconnectIfNoTrackedAnalyses()
    }

    private suspend fun untrackNonUpdatable(vararg analysisIds: String) {
        val nonUpdatableAnalyses =
            analysisIds
                .map { repository.getUpdatedById(it) as? Outcome.Success }
                .mapNotNull { it?.value }
                .filterNot { isAnalysisUpdatableCallback(it) }

        nonUpdatableAnalyses.forEach { processUpdate(it) }
    }

    private suspend fun disconnectIfNoTrackedAnalyses() {
        if (trackedAnalyses.isNotEmpty()) return
        connectionProvider.disconnect(hubMethod)
    }

    private fun handleIncomingUpdate(dto: TDto) {
        val analysis = dto.convertToModel()

        if (!trackedAnalyses.containsKey(analysis.id)) {
            return
        }

        appScope.launch { processUpdate(analysis) }
    }

    private suspend fun processUpdate(analysis: TAnalysis) {
        _updates.emit(analysis)
        if (isAnalysisUpdatableCallback(analysis)) return

        trackedAnalyses.remove(analysis.id)
        disconnectIfNoTrackedAnalyses()
    }
}