package com.openlysis.feature.results

import com.openlysis.data.analysis.core.di.EmailAnalysesRepository
import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.results.components.DetailsScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for displaying details of an email message analysis.
 *
 * @param repository The repository used to fetch and manage message analysis data.
 */
@HiltViewModel
internal class EmailAnalysisDetailsScreenViewModel
    @Inject
    constructor(
        @EmailAnalysesRepository repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>
    ) : DetailsScreenViewModel<MessageAnalysis>(repository)