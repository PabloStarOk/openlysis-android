package com.openlysis.data.database.dao

/**
 * Interface for DAOs that support deletion of oldest entities.
 */
internal interface QueueDao {
    /**
     * Deletes the oldest entity in the queue, according to the DAO's criteria.
     */
    suspend fun deleteOldest()
}