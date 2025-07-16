package com.openlysis.feature.results

import com.openlysis.data.analysis.core.di.EmailAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for managing email analysis previews.
 * Extends [PreviewsScreenViewModel] to handle specific email message analysis functionality.
 *
 * @property repository Repository handling email analysis operations, injected with [EmailAnalysesRepository]
 */
@HiltViewModel
internal class EmailPreviewsScreenViewModel
    @Inject
    constructor(
        @EmailAnalysesRepository repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>
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