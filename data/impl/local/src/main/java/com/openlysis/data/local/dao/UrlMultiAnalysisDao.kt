package com.openlysis.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.openlysis.data.local.entity.analysis.UrlMultiAnalysisEntity
import com.openlysis.data.local.entity.analysis.UrlMultiAnalysisWithAnalyses

/**
 * Data Access Object (DAO) for [UrlMultiAnalysisEntity].
 *
 * Provides database operations for URL multi-analysis entities, including queue-like deletion and paginated retrieval.
 */
@Dao
internal interface UrlMultiAnalysisDao :
    EntityDao<UrlMultiAnalysisEntity>,
    QueueDao {
    /**
     * Deletes the oldest [UrlMultiAnalysisEntity] that has a parent and is not in `Queued` or `InProgress` status.
     * The deletion is based on the minimum `createdAt` timestamp.
     */
    @Query(
        """
            DELETE FROM UrlMultiAnalysisEntity
            WHERE hasParent = 1 
            AND lower(status) != lower("Queued")
            AND lower(status) != lower("InProgress")
            AND createdAt = (
                SELECT MIN(createdAt) 
                FROM UrlMultiAnalysisEntity
                LIMIT 1
            )
        """
    )
    override suspend fun deleteOldest()

    /**
     * Retrieves a paginated list of [UrlMultiAnalysisWithAnalyses].
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [UrlMultiAnalysisWithAnalyses] for the specified page.
     */
    @Transaction
    @Query("SELECT * FROM UrlMultiAnalysisEntity LIMIT :size OFFSET (:size * :page)")
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<UrlMultiAnalysisWithAnalyses>

    /**
     * Retrieves a list of [UrlMultiAnalysisWithAnalyses] by their IDs.
     *
     * @param ids The IDs of the entities to retrieve.
     * @return A list of [UrlMultiAnalysisWithAnalyses] matching the given IDs.
     */
    @Transaction
    @Query("SELECT * FROM UrlMultiAnalysisEntity WHERE id IN (:ids)")
    suspend fun getManyByIds(vararg ids: String): List<UrlMultiAnalysisWithAnalyses>

    /**
     * Counts the number of [UrlMultiAnalysisEntity] entries without a parent.
     *
     * @return The count of entities where `hasParent` is 0.
     */
    @Query("SELECT COUNT(*) FROM UrlMultiAnalysisEntity WHERE hasParent = 0")
    suspend fun countWithoutParent(): Int

    /**
     * Checks if a [UrlMultiAnalysisEntity] with the given ID exists.
     *
     * @param id The ID to check for existence.
     * @return `true` if the entity exists, `false` otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM UrlMultiAnalysisEntity WHERE id = :id)")
    suspend fun exists(id: String): Boolean
}