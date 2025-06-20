package com.openlysis.data.database.dao

/**
 * Defines a DAO to check the existence of an entity by its ID.
 */
internal interface ExistsDao {
    /**
     * Checks if an entity with the given ID exists.
     *
     * @param id The unique identifier of the entity.
     * @return `true` if the entity exists, `false` otherwise.
     */
    suspend fun exists(id: String): Boolean
}