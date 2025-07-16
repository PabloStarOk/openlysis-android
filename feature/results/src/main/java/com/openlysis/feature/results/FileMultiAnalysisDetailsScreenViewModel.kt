package com.openlysis.feature.results

import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for displaying details of a file multi-analysis.
 *
 * @param repository The repository used to fetch and manage [FileMultiAnalysis] data.
 */
@HiltViewModel
internal class FileMultiAnalysisDetailsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>
    ) : DetailsScreenViewModel<FileMultiAnalysis>(repository) {
        override fun getStatus(result: FileMultiAnalysis): AnalysisStatus = result.status
    }