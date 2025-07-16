package com.openlysis.feature.results

import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeUrl
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for displaying details of a URL multi-analysis.
 *
 * @param repository The repository used to fetch and manage [UrlMultiAnalysis] data.
 */
@HiltViewModel
internal class UrlMultiAnalysisDetailsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>
    ) : DetailsScreenViewModel<UrlMultiAnalysis>(repository) {
        override fun getStatus(result: UrlMultiAnalysis): AnalysisStatus = result.status
    }