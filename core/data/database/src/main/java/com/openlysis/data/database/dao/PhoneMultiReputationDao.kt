package com.openlysis.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.openlysis.data.database.entity.reputation.MultiReputationEntity
import com.openlysis.data.database.entity.reputation.PhoneMultiReputationWithReputations

/**
 * Data Access Object (DAO) for [MultiReputationEntity] (phone).
 *
 * Provides database operations for phone multi-reputation entities, including queue-like deletion and paginated retrieval.
 */
@Dao
internal interface PhoneMultiReputationDao :
    EntityDao<MultiReputationEntity>,
    QueueDao,
    RetrievalDao<MultiReputation<PhoneNumberReputation>> {
    /**
     * Deletes the oldest [MultiReputationEntity] records that does not have a parent and match the phone data type.
     *
     * The oldest entities are determined by the minimum `createdAt` value. Only entities with `dataType = "PhoneNumber"` and `hasParent = 0` are considered.
     *
     * @param limit The maximum number of entities to delete.
     */
    @Query(
        """
            DELETE FROM MultiReputationEntity
            WHERE id IN (
                        SELECT id 
                        FROM MultiReputationEntity
                        WHERE dataType IS "PhoneNumber" 
                        AND hasParent = 0
                        ORDER BY createdAt ASC
                        LIMIT :limit
                        )
        """
    )
    override suspend fun deleteOldest(limit: Int)

    /**
     * Retrieves a [PhoneMultiReputationWithReputations] by its ID.
     *
     * @param id The unique identifier of the entity.
     * @return The [PhoneMultiReputationWithReputations] matching the given ID.
     */
    @Transaction
    @Query("SELECT * FROM MultiReputationEntity WHERE id = :id")
    override suspend fun getById(id: String): PhoneMultiReputationWithReputations

    /**
     * Retrieves a paginated list of [PhoneMultiReputationWithReputations].
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [PhoneMultiReputationWithReputations] for the specified page.
     */
    @Transaction
    @Query("SELECT * FROM MultiReputationEntity LIMIT :size OFFSET (:size * :page)")
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<PhoneMultiReputationWithReputations>

    /**
     * Retrieves a list of [PhoneMultiReputationWithReputations] by their IDs.
     *
     * @param ids The IDs of the entities to retrieve.
     * @return A list of [PhoneMultiReputationWithReputations] matching the given IDs.
     */
    @Transaction
    @Query("SELECT * FROM MultiReputationEntity WHERE id IN (:ids)")
    override suspend fun getManyByIds(vararg ids: String): List<PhoneMultiReputationWithReputations>

    /**
     * Counts the number of [MultiReputationEntity] records without a parent.
     *
     * @return The count of entities where `hasParent` is 0.
     */
    @Query("SELECT COUNT(*) FROM MultiReputationEntity WHERE hasParent = 0")
    suspend fun countWithoutParent(): Int

    /**
     * Checks if a [MultiReputationEntity] exists with the given ID.
     *
     * @param id The ID to check for existence.
     * @return `true` if the entity exists, `false` otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM MultiReputationEntity WHERE id = :id)")
    override suspend fun exists(id: String): Boolean
}