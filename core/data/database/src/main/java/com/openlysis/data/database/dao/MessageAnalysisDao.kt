package com.openlysis.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.openlysis.data.database.entity.message.MessageAnalysisEntity
import com.openlysis.data.database.entity.message.MessageAnalysisWithResults

/**
 * Data Access Object (DAO) for [MessageAnalysisEntity].
 *
 * Provides database operations for message analysis entities, including queue-like deletion and paginated retrieval.
 */
@Dao
internal interface MessageAnalysisDao :
    EntityDao<MessageAnalysisEntity>,
    QueueDao {
    /**
     * Deletes the oldest [MessageAnalysisEntity] that is not in `Queued` or `InProgress` status.
     * The deletion is based on the minimum `createdAt` timestamp.
     */
    @Query(
        """
            DELETE FROM MessageAnalysisEntity
            WHERE lower(status) != lower("Queued")
            AND lower(status) != lower("InProgress")
            AND createdAt = (
                SELECT MIN(createdAt)
                FROM MessageAnalysisEntity
                LIMIT 1
            )
        """
    )
    override suspend fun deleteOldest()

    /**
     * Retrieves a [MessageAnalysisWithResults] by its unique ID.
     *
     * @param id The unique identifier of the entity.
     * @return The [MessageAnalysisWithResults] corresponding to the given ID.
     */
    @Transaction
    @Query("SELECT * FROM MessageAnalysisEntity WHERE id = :id")
    suspend fun getById(id: String): MessageAnalysisWithResults

    /**
     * Retrieves a paginated list of [MessageAnalysisWithResults].
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [MessageAnalysisWithResults] for the specified page.
     */
    @Transaction
    @Query("SELECT * FROM MessageAnalysisEntity LIMIT :size OFFSET (:size * :page)")
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<MessageAnalysisWithResults>

    /**
     * Counts the number of [MessageAnalysisEntity] records.
     *
     * @return The total count of message analysis entities.
     */
    @Query("SELECT COUNT(*) FROM MessageAnalysisEntity")
    suspend fun count(): Int

    /**
     * Checks if a [MessageAnalysisEntity] with the given ID exists.
     *
     * @param id The ID to check for existence.
     * @return `true` if the entity exists, `false` otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM MessageAnalysisEntity WHERE id = :id)")
    suspend fun exists(id: String): Boolean
}