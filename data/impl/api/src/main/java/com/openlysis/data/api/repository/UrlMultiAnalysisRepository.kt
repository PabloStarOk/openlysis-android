package com.openlysis.data.api.repository

import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.remote.request.AnalyzeUrl
import com.openlysis.data.remote.response.AnalyzeResponse
import com.openlysis.models.analysis.UrlMultiAnalysis

/**
 * Repository implementation for handling URL multi-analysis operations.
 *
 * @param service The [OpenlysisService] used to perform network operations.
 */
internal class UrlMultiAnalysisRepository(
    service: OpenlysisService
) : BaseAnalysisRepository<AnalyzeUrl, UrlMultiAnalysis>(service) {
    /**
     * Sends an analysis request for a URL.
     *
     * @param request The [AnalyzeUrl] request containing the URL and reanalyze flag.
     * @return The [AnalyzeResponse] from the API.
     */
    override suspend fun handleAnalyze(request: AnalyzeUrl): AnalyzeResponse =
        service.analyzeUrl(
            url = request.url.toString(),
            reanalyze = request.reanalyze
        )

    /**
     * Retrieves the multi-analysis result for a given URL ID.
     *
     * @param id The unique identifier for the URL analysis.
     * @return The [UrlMultiAnalysis] model.
     */
    override suspend fun handleGet(id: String): UrlMultiAnalysis =
        service.getUrlMultiAnalysis(id).convertToModel()
}