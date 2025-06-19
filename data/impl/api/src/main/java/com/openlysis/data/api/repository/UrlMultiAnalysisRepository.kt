package com.openlysis.data.api.repository

import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.remote.request.AnalyzeUrl
import com.openlysis.data.remote.response.AnalyzeResponse
import com.openlysis.models.analysis.UrlMultiAnalysis
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository implementation for handling URL multi-analysis operations.
 *
 * Provides methods to analyze URLs and retrieve multi-analysis results from the remote API.
 *
 * @param service The [OpenlysisService] used to perform network operations.
 */
internal class UrlMultiAnalysisRepository
    @Inject
    constructor(
        service: OpenlysisService
    ) : BaseAnalysisRepository<AnalyzeUrl, UrlMultiAnalysis>(service) {
        /**
         * Analyzes the given URL using the provided [AnalyzeUrl] request.
         *
         * @param request The [AnalyzeUrl] object containing the URL and analysis options.
         * @return A [Response] containing the [AnalyzeResponse] from the API if successful, or an error response otherwise.
         */
        override suspend fun handleAnalyze(request: AnalyzeUrl): Response<AnalyzeResponse> =
            service.analyzeUrl(
                url = request.url.toString(),
                reanalyze = request.reanalyze
            )

        /**
         * Retrieves the multi-analysis result for a given URL ID.
         *
         * @param id The unique identifier for the URL analysis.
         * @return A [Response] containing the [UrlMultiAnalysis] model if successful, or an error response otherwise.
         */
        override suspend fun handleGet(id: String): Response<UrlMultiAnalysis> {
            val response = service.getUrlMultiAnalysis(id)
            return convertToModelIfSuccess(response) { it.convertToModel() }
        }
    }