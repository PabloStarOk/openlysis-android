package com.openlysis.data.local.source

import android.util.Log
import com.openlysis.data.local.Debugging
import com.openlysis.data.local.dao.PhoneMultiReputationDao
import com.openlysis.data.local.dao.PhoneReputationDao
import com.openlysis.data.local.entity.reputation.MultiReputationEntity
import com.openlysis.data.local.entity.reputation.PhoneReputationEntity
import com.openlysis.data.local.entity.reputation.ReputationDataType
import com.openlysis.models.reputation.MultiReputation
import com.openlysis.models.reputation.PhoneNumberReputation

/**
 * Data source for managing [MultiReputation]<[PhoneNumberReputation]> entities and their related reputations in the local database.
 *
 * Handles saving, updating, and retrieving [MultiReputation]<[PhoneNumberReputation]> records, including their associated [PhoneReputationEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param phoneReputationDao The [PhoneReputationDao] for phone reputation operations.
 * @param multiReputationDao The [PhoneMultiReputationDao] for multi-reputation operations.
 */
internal class PhoneMultiReputationDataSource(
    state: LocalDataSourceState,
    private val phoneReputationDao: PhoneReputationDao,
    private val multiReputationDao: PhoneMultiReputationDao
) : RelationalLocalDataSource<MultiReputation<PhoneNumberReputation>>(
        state,
        queueDao = multiReputationDao
    ) {
    /**
     * Saves a list of [MultiReputation]<[PhoneNumberReputation]> and their related reputations to the local database.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of [MultiReputation]<[PhoneNumberReputation]> to save.
     */
    override suspend fun save(
        parentId: String?,
        models: List<MultiReputation<PhoneNumberReputation>>
    ) {
        val (reputationEntities, multiReputationEntities) = convertToEntities(parentId, models)
        multiReputationDao.add(*multiReputationEntities)
        phoneReputationDao.add(*reputationEntities)

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(
                LOG_TAG,
                "A total of ${multiReputationEntities.size} ${MultiReputation::class.java.simpleName} (phone) were added."
            )
        }

        models.forEach { m ->
            Debugging.logVerboseMultiReputation(
                LOG_TAG,
                contextMsg = "${MultiReputation::class.java.simpleName} (phone) added.",
                m
            )
        }
    }

    /**
     * Updates a list of [MultiReputation]<[PhoneNumberReputation]> and their related reputations in the local database.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of [MultiReputation]<[PhoneNumberReputation]> to update.
     */
    override suspend fun update(
        parentId: String?,
        models: List<MultiReputation<PhoneNumberReputation>>
    ) {
        val (reputationEntities, multiReputationEntities) = convertToEntities(parentId, models)
        multiReputationDao.update(*multiReputationEntities)
        phoneReputationDao.update(*reputationEntities)

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(
                LOG_TAG,
                "A total of ${multiReputationEntities.size} ${MultiReputation::class.java.simpleName} (phone) were added."
            )
        }

        models.forEach { m ->
            Debugging.logVerboseMultiReputation(
                LOG_TAG,
                contextMsg = "${MultiReputation::class.java.simpleName} (phone) updated.",
                m
            )
        }
    }

    /**
     * Retrieves a list of [MultiReputation]<[PhoneNumberReputation]> by their IDs, including their related reputations.
     *
     * @param ids The IDs of the [MultiReputation]<[PhoneNumberReputation]> to retrieve.
     * @return A list of [MultiReputation]<[PhoneNumberReputation]> records.
     */
    override suspend fun getManyByIds(
        vararg ids: String
    ): List<MultiReputation<PhoneNumberReputation>> {
        val multiReputationWithReputations = multiReputationDao.getManyByIds(*ids)
        return multiReputationWithReputations.map { m -> m.buildMultiReputation() }
    }

    /**
     * Retrieves a paginated list of [MultiReputation]<[PhoneNumberReputation]> records with their related reputations.
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [MultiReputation]<[PhoneNumberReputation]> records for the specified page.
     */
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<MultiReputation<PhoneNumberReputation>> {
        val multiReputationWithReputations = multiReputationDao.getMany(page, size)
        return multiReputationWithReputations.map { m -> m.buildMultiReputation() }
    }

    /**
     * Checks if a [MultiReputation]<[PhoneNumberReputation]> exists in the local database.
     *
     * @param model The [MultiReputation]<[PhoneNumberReputation]> to check.
     * @return `true` if the entity exists, `false` otherwise.
     */
    override suspend fun exists(model: MultiReputation<PhoneNumberReputation>): Boolean =
        multiReputationDao.exists(model.id)

    /**
     * Converts models to their corresponding [PhoneReputationEntity] and [MultiReputationEntity] arrays.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of [MultiReputation]<[PhoneNumberReputation]> to convert.
     * @return A pair of arrays: [PhoneReputationEntity] and [MultiReputationEntity].
     */
    private fun convertToEntities(
        parentId: String?,
        models: List<MultiReputation<PhoneNumberReputation>>
    ): Pair<Array<PhoneReputationEntity>, Array<MultiReputationEntity>> =
        Pair(
            models
                .map { m ->
                    m.reputations
                        .map { a ->
                            PhoneReputationEntity.createFromModel(a, m.id)
                        }
                }.flatten()
                .toTypedArray(),
            models
                .map { m ->
                    MultiReputationEntity.createFromModel(
                        model = m,
                        dataType = ReputationDataType.PhoneNumber,
                        messageAnalysisId = parentId
                    )
                }.toTypedArray()
        )

    private companion object {
        private val LOG_TAG = PhoneMultiReputationDataSource::class.java.simpleName
    }
}