package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.openlysis.data.database.Debugging
import com.openlysis.data.database.dao.PhoneMultiReputationDao
import com.openlysis.data.database.dao.PhoneReputationDao
import com.openlysis.data.database.di.PhoneMultiReputationDsState
import com.openlysis.data.database.entity.reputation.MultiReputationEntity
import com.openlysis.data.database.entity.reputation.PhoneReputationEntity
import com.openlysis.data.database.entity.reputation.ReputationDataType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for managing [MultiReputation]<[PhoneNumberReputation]> entities and their related reputations in the local database.
 *
 * Handles saving, updating, and retrieving [MultiReputation]<[PhoneNumberReputation]> records, including their associated [PhoneReputationEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param phoneReputationDao The [PhoneReputationDao] for phone reputation operations.
 * @param multiReputationDao The [PhoneMultiReputationDao] for multi-reputation operations.
 */
@Singleton
internal class PhoneMultiReputationsLocalDataSource
    @Inject
    constructor(
        @PhoneMultiReputationDsState state: LocalDataSourceState,
        private val phoneReputationDao: PhoneReputationDao,
        private val multiReputationDao: PhoneMultiReputationDao
    ) : RelationalLocalDataSource<MultiReputation<PhoneNumberReputation>>(
            state,
            queueDao = multiReputationDao,
            retrievalDao = multiReputationDao
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
            private val LOG_TAG =
                PhoneMultiReputationsLocalDataSource::class.java.simpleName.take(23)
        }
    }