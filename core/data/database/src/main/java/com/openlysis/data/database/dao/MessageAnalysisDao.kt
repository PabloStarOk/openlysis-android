package com.openlysis.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.openlysis.data.analysis.model.message.MessageType
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
    ExistsDao,
    QueueDao {
    /**
     * Deletes the oldest [MessageAnalysisEntity] records.
     *
     * The deletion is based on the minimum `createdAt` timestamp.
     *
     * @param limit The maximum number of entities to delete.
     */
    @Query(
        """
            DELETE FROM MessageAnalysisEntity
            WHERE id IN (
                        SELECT id 
                        FROM MessageAnalysisEntity
                        ORDER BY createdAt ASC
                        LIMIT :limit
            )
        """
    )
    override suspend fun deleteOldest(limit: Int)

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
     * @param type A [MessageType] representing the type of message to retrieve.
     * @return A list of [MessageAnalysisWithResults] for the specified page.
     */
    @Transaction
    @Query(
        """
            SELECT * FROM MessageAnalysisEntity
            WHERE message_type = :type
            LIMIT :size OFFSET (:size * :page)
        """
    )
    suspend fun getMany(
        page: Int,
        size: Int,
        type: MessageType
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
    override suspend fun exists(id: String): Boolean
}