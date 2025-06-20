package com.openlysis.data.analysis.core.repository

import com.openlysis.data.analysis.core.error.Outcome

/**
 * Repository interface for analyzing data and retrieving analysis results.
 *
 * @param TRequest The type of the request object for analysis.
 * @param TModel The type of the model returned by the repository.
 */
interface AnalysesRepository<TRequest, TModel>
    where TRequest : Any,
          TModel : Any {
    /**
     * Analyzes the provided request and returns the analysis result.
     *
     * @param request The request object containing data to be analyzed.
     * @return An [Outcome] containing the initial analysis results of type [TModel].
     *         If the outcome is a failure, it will contain an error.
     */
    suspend fun analyze(request: TRequest): Outcome<TModel>

    /**
     * Retrieves a model by its unique identifier.
     *
     * @param id The unique identifier of the model.
     * @return An [Outcome] containing the model of type [TModel].
     *         If the outcome is a failure, it will contain an error.
     */
    suspend fun getById(id: String): Outcome<TModel>

    /**
     * Retrieves the most recently updated model by its unique identifier.
     *
     * @param id The unique identifier of the model.
     * @return An [Outcome] containing the updated model of type [TModel].
     *         If the outcome is a failure, it will contain an error.
     */
    suspend fun getUpdatedById(id: String): Outcome<TModel>

    /**
     * Retrieves a paginated list of models.
     *
     * @param page The page number to retrieve.
     * @return A list of models of type [TModel] for the specified page.
     */
    suspend fun getManyPaged(page: Int): List<TModel>
}