package com.openlysis.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.database.entity.analysis.FileMultiAnalysisEntity
import com.openlysis.data.database.entity.analysis.FileMultiAnalysisWithAnalyses

/**
 * Data Access Object (DAO) for [FileMultiAnalysisEntity].
 *
 * Provides database operations for file multi-analysis entities, including queue-like deletion and paginated retrieval.
 */
@Dao
internal interface FileMultiAnalysisDao :
    EntityDao<FileMultiAnalysisEntity>,
    QueueDao,
    RetrievalDao<FileMultiAnalysis> {
    /**
     * Deletes the oldest [FileMultiAnalysisEntity] records that have a parent and are not in `Queued` or `InProgress` status.
     *
     * The deletion is based on the minimum `createdAt` timestamp. Only entities with `hasParent = 1` and a status other than `Queued` or `InProgress` are considered.
     *
     * @param limit The maximum number of entities to delete.
     */
    @Query(
        """
            DELETE FROM FileMultiAnalysisEntity
            WHERE id IN (
                        SELECT id 
                        FROM FileMultiAnalysisEntity
                        WHERE hasParent = 1 
                        AND lower(status) NOT IN (lower("Queued"), lower("InProgress"))
                        ORDER BY createdAt ASC
                        LIMIT :limit
            )
        """
    )
    override suspend fun deleteOldest(limit: Int)

    /**
     * Retrieves a [FileMultiAnalysisWithAnalyses] by its ID.
     *
     * @param id The unique identifier of the entity.
     * @return The [FileMultiAnalysisWithAnalyses] corresponding to the given ID.
     */
    @Transaction
    @Query("SELECT * FROM FileMultiAnalysisEntity WHERE id = :id")
    override suspend fun getById(id: String): FileMultiAnalysisWithAnalyses

    /**
     * Retrieves a paginated list of [FileMultiAnalysisWithAnalyses].
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [FileMultiAnalysisWithAnalyses] for the specified page.
     */
    @Transaction
    @Query("SELECT * FROM FileMultiAnalysisEntity LIMIT :size OFFSET (:size * :page)")
    override suspend fun getMany(
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
    override suspend fun getManyByIds(vararg ids: String): List<FileMultiAnalysisWithAnalyses>

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
    override suspend fun exists(id: String): Boolean
}