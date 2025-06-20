package com.openlysis.data.database.dao

import com.openlysis.data.database.entity.BuildablePojo

/**
 * Defines a DAO for retrieving entities and information from the DB.
 *
 * @param TModel The type of the model to be retrieved.
 */
internal interface RetrievalDao<TModel> where TModel : Any {
    /**
     * Retrieves a single entity by its unique identifier.
     *
     * @param id The unique identifier of the entity.
     * @return The entity wrapped in a [BuildablePojo].
     */
    suspend fun getById(id: String): BuildablePojo<TModel>

    /**
     * Retrieves a paginated list of entities.
     *
     * @param page The page number (zero-based).
     * @param size The number of entities per page.
     * @return A list of entities wrapped in [BuildablePojo].
     */
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<BuildablePojo<TModel>>

    /**
     * Retrieves multiple entities by their unique identifiers.
     *
     * @param ids The unique identifiers of the entities.
     * @return A list of entities wrapped in [BuildablePojo].
     */
    suspend fun getManyByIds(vararg ids: String): List<BuildablePojo<TModel>>

    /**
     * Checks if an entity exists by its unique identifier.
     *
     * @param id The unique identifier of the entity.
     * @return `true` if the entity exists, `false` otherwise.
     */
    suspend fun exists(id: String): Boolean
}