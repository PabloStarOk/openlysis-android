package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeFile
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * ViewModel for displaying details of a file multi-analysis.
 *
 * @param repository The repository used to fetch and manage [FileMultiAnalysis] data.
 * @property updateTracker Tracker for analysis updates
 * @param appScope The application-level coroutine scope.
 */
@HiltViewModel
internal class FileMultiAnalysisDetailsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        updateTracker: AnalysisUpdateTracker<FileMultiAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ) : DetailsScreenViewModel<FileMultiAnalysis>(repository, updateTracker, appScope) {
        override fun getStatus(result: FileMultiAnalysis): AnalysisStatus = result.status
    }