package com.openlysis.data.analysis.source

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.response.AnalyzeResponse

/**
 * A remote data source to perform and retrieve analyses.
 *
 * @param TRequest The type of the request to start the analyses.
 * @param TModel The type of the model returned by data retrieval methods.
 */
interface AnalysesRemoteDataSource<TRequest, TModel>
    where TRequest : Any, TModel : Model {
    /**
     * Starts an analysis with the given request.
     *
     * @param request The request object containing analysis parameters.
     * @return An [Outcome] wrapping the [AnalyzeResponse].
     */
    suspend fun analyze(request: TRequest): Outcome<AnalyzeResponse>

    /**
     * Retrieves a model by its unique identifier.
     *
     * @param id The unique identifier of the model.
     * @return An [Outcome] wrapping the model of type [TModel].
     */
    suspend fun getById(id: String): Outcome<TModel>

    /**
     * Retrieves multiple models with pagination.
     *
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return An [Outcome] wrapping the model(s) of type [TModel].
     */
    suspend fun getMany(
        page: Int,
        size: Int
    ): Outcome<List<TModel>>
}