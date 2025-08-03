package com.openlysis.feature.results

import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeUrl
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for displaying previews of url analyses.
 *
 * @property repository The repository used to fetch and manage file multi-analysis data.
 */
@HiltViewModel
internal class UrlMultiAnalysisPreviewsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>
    ) : PreviewsScreenViewModel<UrlMultiAnalysis>(repository) {
        override fun convertToPreview(result: UrlMultiAnalysis): AnalysisPreviewState =
            AnalysisPreviewState(
                id = result.id,
                headerContent = result.url.toString(),
                startedDate = result.startedDate,
                status = result.status,
                verdict = result.finalVerdict
            )
    }