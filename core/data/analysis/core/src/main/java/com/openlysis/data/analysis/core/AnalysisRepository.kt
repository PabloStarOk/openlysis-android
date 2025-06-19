package com.openlysis.data.analysis.core

import com.openlysis.data.analysis.core.error.Outcome
import com.openlysis.data.analysis.core.response.AnalyzeResponse

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
     * Analyzes the given request and returns the analysis outcome.
     *
     * @param request The request object to analyze.
     * @return An [Outcome] containing the [AnalyzeResponse].
     */
    suspend fun analyze(request: TRequest): Outcome<AnalyzeResponse>

    /**
     * Retrieves a model by its unique identifier.
     *
     * @param id The unique identifier of the model.
     * @return An [Outcome] containing the model of type [TModel].
     */
    suspend fun get(id: String): Outcome<TModel>
}