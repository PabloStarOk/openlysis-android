package com.openlysis.data.analysis.core.source

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.common.Model

/**
 * Interface for local data source operations on analysis models.
 *
 * @param TModel The type of the model to be persisted and retrieved.
 */
interface AnalysesLocalDataSource<TModel> where TModel : Model {
    /**
     * Saves a model to the local data source.
     *
     * @param model The model to save.
     */
    suspend fun save(model: TModel)

    /**
     * Updates an existing model in the local data source.
     *
     * @param model The model to update.
     */
    suspend fun update(model: TModel)

    /**
     * Checks if a [TModel] exists in the local data source.
     *
     * @param model The model instance to check for existence.
     * @return `true` if the model exists, `false` otherwise.
     */
    suspend fun exists(model: TModel): Boolean

    /**
     * Retrieves a model by its unique identifier.
     *
     * @param id The unique identifier of the model.
     * @return An [Outcome] containing the model if found, or an error.
     */
    suspend fun getById(id: String): Outcome<TModel>

    /**
     * Retrieves a paginated list of models.
     *
     * @param page The page number to retrieve (zero-based).
     * @param size The number of items per page.
     * @return A list of models for the specified page.
     */
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<TModel>
}