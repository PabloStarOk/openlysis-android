package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeUrl
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * ViewModel for displaying details of a URL multi-analysis.
 *
 * @param repository The repository used to fetch and manage [UrlMultiAnalysis] data.
 * @property updateTracker Tracker for analysis updates
 * @param appScope The application-level coroutine scope.
 */
@HiltViewModel
internal class UrlMultiAnalysisDetailsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>,
        updateTracker: AnalysisUpdateTracker<UrlMultiAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ) : DetailsScreenViewModel<UrlMultiAnalysis>(repository, updateTracker, appScope) {
        override fun getStatus(result: UrlMultiAnalysis): AnalysisStatus = result.status
    }