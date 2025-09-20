package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.di.MessageAnalysesRepository
import com.openlysis.data.analysis.di.MessageAnalysisUpdateTracker
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * ViewModel responsible for managing email analysis previews.
 * Extends [PreviewsScreenViewModel] to handle specific email message analysis functionality.
 *
 * @property repository Repository handling email analysis operations, injected with [EmailAnalysesRepository]
 * @property updateTracker Tracker for analysis updates
 * @property appScope Application-level coroutine scope
 */
@HiltViewModel
internal class EmailPreviewsScreenViewModel
    @Inject
    constructor(
        @MessageAnalysesRepository(MessageType.Email)
        repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @MessageAnalysisUpdateTracker(MessageType.Email)
        updateTracker: AnalysisUpdateTracker<MessageAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ) : PreviewsScreenViewModel<MessageAnalysis>(repository, updateTracker, appScope) {
        override fun handlePreviewConversion(result: MessageAnalysis): AnalysisPreviewState =
            AnalysisPreviewState(
                id = result.id,
                headerContent = result.message.sender,
                startedDate = result.startedDate,
                status = result.status,
                verdict = result.verdict
            )
    }