package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.di.MessageAnalysisDependency
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
 * ViewModel for the SMS Previews screen.
 *
 * Injects an [AnalysesRepository] for analyzing SMS messages.
 *
 * @param repository The repository used to fetch and analyze SMS messages.
 * @property updateTracker Tracker for analysis updates
 * @property appScope Application-level coroutine scope
 */
@HiltViewModel
internal class SmsPreviewsScreenViewModel
    @Inject
    constructor(
        @MessageAnalysisDependency(MessageType.Sms)
        repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @MessageAnalysisDependency(MessageType.Sms)
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