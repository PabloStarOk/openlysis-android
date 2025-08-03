package com.openlysis.data.remote.source

import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.request.AnalyzeUrl
import com.openlysis.data.analysis.response.AnalyzeResponse
import com.openlysis.data.remote.OpenlysisApi
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source implementation for handling URL multi-analysis operations.
 *
 * Provides methods to analyze URLs and retrieve multi-analysis results from the remote API.
 *
 * @param api The [OpenlysisApi] used to perform network operations.
 * @param ioDispatcher The coroutine dispatcher used for network operations.
 */
internal class UrlMultiAnalysesRemoteDataSource
    @Inject
    constructor(
        api: OpenlysisApi,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ) : BaseAnalysesRemoteDataSource<AnalyzeUrl, UrlMultiAnalysis>(ioDispatcher, api) {
        /**
         * Analyzes the given URL using the provided [AnalyzeUrl] request.
         *
         * @param request The [AnalyzeUrl] object containing the URL and analysis options.
         * @return A [Response] containing the [AnalyzeResponse] from the API if successful, or an error response otherwise.
         */
        override suspend fun handleAnalyze(request: AnalyzeUrl): Response<AnalyzeResponse> =
            api.analyzeUrl(
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
            val response = api.getUrlMultiAnalysis(id)
            return convertToModelIfSuccess(response) { it.convertToModel() }
        }

        /**
         * Retrieves a paginated list of URL multi-analysis results.
         *
         * @param page The page number to retrieve.
         * @param size The number of items per page.
         * @return A [Response] containing a list of [UrlMultiAnalysis] models if successful, or an error response otherwise.
         */
        override suspend fun handleGetMany(
            page: Int,
            size: Int
        ): Response<List<UrlMultiAnalysis>> {
            val response =
                api.getUrlMultiAnalyses(
                    page = page,
                    pageSize = size
                )
            return convertToModelIfSuccess(response) {
                it.analyses.map { a ->
                    a.convertToModel()
                }
            }
        }
    }