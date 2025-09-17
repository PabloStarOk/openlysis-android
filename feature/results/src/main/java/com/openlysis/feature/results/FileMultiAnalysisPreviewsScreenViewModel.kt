package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeFile
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import com.openlysis.feature.results.components.preview.AnalysisPreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * ViewModel for displaying previews of file analyses.
 *
 * @property repository The repository used to fetch and manage file multi-analysis data.
 * @property updateTracker Tracker for analysis updates
 * @property appScope Application-level coroutine scope
 */
@HiltViewModel
internal class FileMultiAnalysisPreviewsScreenViewModel
    @Inject
    constructor(
        repository: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        updateTracker: AnalysisUpdateTracker<FileMultiAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ) : PreviewsScreenViewModel<FileMultiAnalysis>(repository, updateTracker, appScope) {
        override fun convertToPreview(result: FileMultiAnalysis): AnalysisPreviewState =
            AnalysisPreviewState(
                id = result.id,
                headerContent = result.fileMetadata.name,
                startedDate = result.startedDate,
                status = result.status,
                verdict = result.finalVerdict
            )
    }