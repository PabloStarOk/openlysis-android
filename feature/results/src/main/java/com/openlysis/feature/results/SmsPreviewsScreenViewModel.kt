package com.openlysis.feature.results

import com.openlysis.data.analysis.di.SmsAnalysesRepository
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the SMS Previews screen.
 *
 * Injects an [AnalysesRepository] for analyzing SMS messages.
 *
 * @param repository The repository used to fetch and analyze SMS messages.
 */
@HiltViewModel
internal class SmsPreviewsScreenViewModel
    @Inject
    constructor(
        @SmsAnalysesRepository repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>
    ) : PreviewsScreenViewModel<MessageAnalysis>(repository = repository) {
        override fun convertToPreview(result: MessageAnalysis): AnalysisPreviewState =
            AnalysisPreviewState(
                id = result.id,
                headerContent = result.message.sender,
                startedDate = result.startedDate,
                status = result.status,
                verdict = result.verdict
            )
    }