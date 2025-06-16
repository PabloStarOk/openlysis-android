package com.openlysis.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.openlysis.data.local.entity.analysis.FileMultiAnalysisEntity
import com.openlysis.data.local.entity.analysis.FileMultiAnalysisWithAnalyses

/**
 * Data Access Object (DAO) for [FileMultiAnalysisEntity].
 *
 * Provides database operations for file multi-analysis entities, including queue-like deletion and paginated retrieval.
 */
@Dao
internal interface FileMultiAnalysisDao :
    EntityDao<FileMultiAnalysisEntity>,
    QueueDao {
    /**
     * Deletes the oldest [FileMultiAnalysisEntity] that has a parent and is not in `Queued` or `InProgress` status.
     * The deletion is based on the minimum `createdAt` timestamp.
     */
    @Query(
        """
            DELETE FROM FileMultiAnalysisEntity
            WHERE hasParent = 1 
            AND lower(status) != lower("Queued")
            AND lower(status) != lower("InProgress")
            AND createdAt = (
                SELECT MIN(createdAt) 
                FROM FileMultiAnalysisEntity
                LIMIT 1
            )
        """
    )
    override suspend fun deleteOldest()

    /**
     * Retrieves a paginated list of [FileMultiAnalysisWithAnalyses].
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [FileMultiAnalysisWithAnalyses] for the specified page.
     */
    @Transaction
    @Query("SELECT * FROM FileMultiAnalysisEntity LIMIT :size OFFSET (:size * :page)")
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<FileMultiAnalysisWithAnalyses>

    /**
     * Retrieves a list of [FileMultiAnalysisWithAnalyses] by their IDs.
     *
     * @param ids The IDs of the entities to retrieve.
     * @return A list of [FileMultiAnalysisWithAnalyses] matching the given IDs.
     */
    @Transaction
    @Query("SELECT * FROM FileMultiAnalysisEntity WHERE id IN (:ids)")
    suspend fun getManyByIds(vararg ids: String): List<FileMultiAnalysisWithAnalyses>

    /**
     * Counts the number of [FileMultiAnalysisEntity] entries without a parent.
     *
     * @return The count of entities where `hasParent` is 0.
     */
    @Query("SELECT COUNT(*) FROM FileMultiAnalysisEntity WHERE hasParent = 0")
    suspend fun countWithoutParent(): Int

    /**
     * Checks if a [FileMultiAnalysisEntity] with the given ID exists.
     *
     * @param id The ID to check for existence.
     * @return `true` if the entity exists, `false` otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM FileMultiAnalysisEntity WHERE id = :id)")
    suspend fun exists(id: String): Boolean
}