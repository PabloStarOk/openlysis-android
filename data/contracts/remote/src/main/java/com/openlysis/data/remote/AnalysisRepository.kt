package com.openlysis.data.remote

import com.openlysis.data.remote.response.AnalyzeResponse
import kotlin.Result

/**
 * Repository interface for analyzing data and retrieving analysis results.
 *
 * @param TRequest The type of the request object for analysis.
 * @param TModel The type of the model returned by the repository.
 */
interface AnalysisRepository<TRequest, TModel>
    where TRequest : Any,
          TModel : Any {
    /**
     * Analyzes the given request and returns the analysis response.
     *
     * @param request The request object to analyze.
     * @return A [Result] containing [AnalyzeResponse] on success, or an error on failure.
     */
    suspend fun analyze(request: TRequest): Result<AnalyzeResponse>

    /**
     * Retrieves a model by its unique identifier.
     *
     * @param id The unique identifier of the model.
     * @return A [Result] containing the model of type [TModel] on success, or an error on failure.
     */
    suspend fun get(id: String): Result<TModel>
}