package com.openlysis.data.local

/**
 * Repository interface for local storage operations on model data.
 *
 * @param TModel The type of model entity.
 */
interface LocalRepository<TModel> where TModel : Any {
    /**
     * Saves the given model entity to local storage.
     *
     * @param model The model entity to save.
     */
    suspend fun save(model: TModel)

    /**
     * Updates the given model entity in local storage.
     *
     * @param model The model entity to update.
     */
    suspend fun update(model: TModel)

    /**
     * Retrieves a paginated list of model entities from local storage.
     *
     * @param page The page number to retrieve.
     * @param size The number of entities per page.
     * @return A list of model entities.
     */
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<TModel>
}