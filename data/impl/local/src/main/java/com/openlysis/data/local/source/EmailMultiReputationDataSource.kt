package com.openlysis.data.local.source

import android.util.Log
import com.openlysis.data.local.Debugging
import com.openlysis.data.local.dao.EmailMultiReputationDao
import com.openlysis.data.local.dao.EmailReputationDao
import com.openlysis.data.local.entity.reputation.EmailReputationEntity
import com.openlysis.data.local.entity.reputation.MultiReputationEntity
import com.openlysis.data.local.entity.reputation.ReputationDataType
import com.openlysis.models.reputation.EmailAddressReputation
import com.openlysis.models.reputation.MultiReputation

/**
 * Data source for managing [MultiReputation]<[EmailAddressReputation]> entities and their related reputations in the local database.
 *
 * Handles saving, updating, and retrieving [MultiReputation]<[EmailAddressReputation]> records, including their associated [EmailReputationEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param emailReputationDao The [EmailReputationDao] for email reputation operations.
 * @param multiReputationDao The [EmailMultiReputationDao] for multi-reputation operations.
 */
internal class EmailMultiReputationDataSource(
    state: LocalDataSourceState,
    private val emailReputationDao: EmailReputationDao,
    private val multiReputationDao: EmailMultiReputationDao
) : RelationalLocalDataSource<MultiReputation<EmailAddressReputation>>(
        state,
        queueDao = multiReputationDao
    ) {
    /**
     * Saves a list of [MultiReputation]<[EmailAddressReputation]> and their related reputations to the local database.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of [MultiReputation]<[EmailAddressReputation]> to save.
     */
    override suspend fun save(
        parentId: String?,
        models: List<MultiReputation<EmailAddressReputation>>
    ) {
        val (reputationEntities, multiReputationEntities) = convertToEntities(parentId, models)
        multiReputationDao.add(*multiReputationEntities)
        emailReputationDao.add(*reputationEntities)

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(
                LOG_TAG,
                "A total of ${multiReputationEntities.size} ${MultiReputation::class.java.simpleName} (email) were added."
            )
        }

        models.forEach { m ->
            Debugging.logVerboseMultiReputation(
                LOG_TAG,
                contextMsg = "${MultiReputation::class.java.simpleName} (email) added.",
                m
            )
        }
    }

    /**
     * Updates a list of [MultiReputation]<[EmailAddressReputation]> and their related reputations in the local database.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of [MultiReputation]<[EmailAddressReputation]> to update.
     */
    override suspend fun update(
        parentId: String?,
        models: List<MultiReputation<EmailAddressReputation>>
    ) {
        val (reputationEntities, multiReputationEntities) = convertToEntities(parentId, models)
        multiReputationDao.update(*multiReputationEntities)
        emailReputationDao.update(*reputationEntities)

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(
                LOG_TAG,
                "A total of ${multiReputationEntities.size} ${MultiReputation::class.java.simpleName} (email) were updated."
            )
        }

        models.forEach { m ->
            Debugging.logVerboseMultiReputation(
                LOG_TAG,
                contextMsg = "${MultiReputation::class.java.simpleName} (email) updated.",
                m
            )
        }
    }

    /**
     * Retrieves a list of [MultiReputation]<[EmailAddressReputation]> by their IDs, including their related reputations.
     *
     * @param ids The IDs of the [MultiReputation]<[EmailAddressReputation]> to retrieve.
     * @return A list of [MultiReputation]<[EmailAddressReputation]> records.
     */
    override suspend fun getManyByIds(
        vararg ids: String
    ): List<MultiReputation<EmailAddressReputation>> {
        val multiReputationWithReputations = multiReputationDao.getManyByIds(*ids)
        return multiReputationWithReputations.map { m -> m.buildMultiReputation() }
    }

    /**
     * Retrieves a paginated list of [MultiReputation]<[EmailAddressReputation]> records with their related reputations.
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [MultiReputation]<[EmailAddressReputation]> records for the specified page.
     */
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<MultiReputation<EmailAddressReputation>> {
        val multiReputationWithReputations = multiReputationDao.getMany(page, size)
        return multiReputationWithReputations.map { m -> m.buildMultiReputation() }
    }

    /**
     * Checks if a [MultiReputation]<[EmailAddressReputation]> exists in the local database.
     *
     * @param model The [MultiReputation]<[EmailAddressReputation]> to check.
     * @return `true` if the entity exists, `false` otherwise.
     */
    override suspend fun exists(model: MultiReputation<EmailAddressReputation>): Boolean =
        multiReputationDao.exists(model.id)

    /**
     * Converts models to their corresponding [EmailReputationEntity] and [MultiReputationEntity] arrays.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of [MultiReputation]<[EmailAddressReputation]> to convert.
     * @return A pair of arrays: [EmailReputationEntity] and [MultiReputationEntity].
     */
    private fun convertToEntities(
        parentId: String?,
        models: List<MultiReputation<EmailAddressReputation>>
    ): Pair<Array<EmailReputationEntity>, Array<MultiReputationEntity>> =
        Pair(
            models
                .map { m ->
                    m.reputations
                        .map { a ->
                            EmailReputationEntity.createFromModel(a, m.id)
                        }
                }.flatten()
                .toTypedArray(),
            models
                .map { m ->
                    MultiReputationEntity.createFromModel(
                        model = m,
                        dataType = ReputationDataType.EmailAddress,
                        messageAnalysisId = parentId
                    )
                }.toTypedArray()
        )

    private companion object {
        private val LOG_TAG = EmailMultiReputationDataSource::class.java.simpleName
    }
}