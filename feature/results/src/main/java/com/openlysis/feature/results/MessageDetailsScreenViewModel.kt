package com.openlysis.feature.results

import com.openlysis.data.analysis.di.EmailAnalysesRepository
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for displaying details of a message analysis.
 *
 * @param repository The repository used to fetch and manage message analysis data, which can be either an email or SMS message analysis repository.
 */
@HiltViewModel
internal class MessageDetailsScreenViewModel
    @Inject
    constructor(
        @EmailAnalysesRepository repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>
    ) : DetailsScreenViewModel<MessageAnalysis>(repository) {
        override fun getStatus(result: MessageAnalysis): AnalysisStatus = result.status
    }