package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.database.Debugging
import com.openlysis.data.database.dao.EmailMultiReputationDao
import com.openlysis.data.database.dao.EmailReputationDao
import com.openlysis.data.database.di.EmailMultiReputationDsState
import com.openlysis.data.database.entity.reputation.EmailReputationEntity
import com.openlysis.data.database.entity.reputation.MultiReputationEntity
import com.openlysis.data.database.entity.reputation.ReputationDataType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for managing [MultiReputation]<[EmailAddressReputation]> entities and their related reputations in the local database.
 *
 * Handles saving, updating, and retrieving [MultiReputation]<[EmailAddressReputation]> records, including their associated [EmailReputationEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param emailReputationDao The [EmailReputationDao] for email reputation operations.
 * @param multiReputationDao The [EmailMultiReputationDao] for multi-reputation operations.
 */
@Singleton
internal class EmailMultiReputationsLocalDataSource
    @Inject
    constructor(
        @EmailMultiReputationDsState state: LocalDataSourceState,
        private val emailReputationDao: EmailReputationDao,
        private val multiReputationDao: EmailMultiReputationDao
    ) : RelationalLocalDataSource<MultiReputation<EmailAddressReputation>>(
            state,
            queueDao = multiReputationDao,
            retrievalDao = multiReputationDao
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
            private val LOG_TAG =
                EmailMultiReputationsLocalDataSource::class.java.simpleName.take(23)
        }
    }