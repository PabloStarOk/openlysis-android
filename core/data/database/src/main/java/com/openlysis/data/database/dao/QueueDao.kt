package com.openlysis.data.database.dao

/**
 * Interface for DAOs that support deletion of oldest entities.
 */
internal interface QueueDao {
    /**
     * Removes the specified number of oldest entities from the queue.
     *
     * @param limit The maximum number of oldest entities to remove. Defaults to 1.
     */
    suspend fun deleteOldest(limit: Int = 1)
}