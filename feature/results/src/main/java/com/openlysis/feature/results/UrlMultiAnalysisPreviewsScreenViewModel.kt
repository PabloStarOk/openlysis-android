package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeUrl
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * ViewModel for displaying previews of url analyses.
 *
 * @property repository The repository used to fetch and manage file multi-analysis data.
 * @property updateTracker Tracker for analysis updates
 * @property appScope Application-level coroutine scope
 */
@HiltViewModel
internal class UrlMultiAnalysisPreviewsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>,
        updateTracker: AnalysisUpdateTracker<UrlMultiAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ) : PreviewsScreenViewModel<UrlMultiAnalysis>(repository, updateTracker, appScope) {
        override fun handlePreviewConversion(result: UrlMultiAnalysis): AnalysisPreviewState =
            AnalysisPreviewState(
                id = result.id,
                headerContent = result.url.toString(),
                startedDate = result.startedDate,
                status = result.status,
                verdict = result.finalVerdict
            )
    }