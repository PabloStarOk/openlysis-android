package com.openlysis.feature.results

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.analysis.di.MessageAnalysisDependency
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * ViewModel for displaying details of a message analysis.
 *
 * @param repository The repository used to fetch and manage SMS analysis data.
 * @property updateTracker Tracker for analysis updates
 * @param appScope The application-level coroutine scope.
 */
@HiltViewModel
internal class SmsDetailsScreenViewModel
    @Inject
    constructor(
        @MessageAnalysisDependency(MessageType.Sms)
        repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @MessageAnalysisDependency(MessageType.Sms)
        updateTracker: AnalysisUpdateTracker<MessageAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ) : DetailsScreenViewModel<MessageAnalysis>(repository, updateTracker, appScope) {
        override fun getStatus(result: MessageAnalysis): AnalysisStatus = result.status
    }