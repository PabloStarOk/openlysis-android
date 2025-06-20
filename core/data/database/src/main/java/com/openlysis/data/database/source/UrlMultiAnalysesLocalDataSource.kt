package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.analysis.core.error.Outcome
import com.openlysis.data.analysis.core.error.RepositoryError
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.database.Debugging
import com.openlysis.data.database.dao.UrlAnalysisDao
import com.openlysis.data.database.dao.UrlMultiAnalysisDao
import com.openlysis.data.database.di.UrlMultiAnalysisDsState
import com.openlysis.data.database.entity.analysis.UrlAnalysisEntity
import com.openlysis.data.database.entity.analysis.UrlMultiAnalysisEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for managing [UrlMultiAnalysis] entities and their related analyses in the local database.
 *
 * Handles saving, updating, and retrieving [UrlMultiAnalysis] records, including their associated [UrlAnalysisEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param urlAnalysisDao The [UrlAnalysisDao] for URL analysis operations.
 * @param multiAnalysisDao The [UrlMultiAnalysisDao] for multi-analysis operations.
 */
@Singleton
internal class UrlMultiAnalysesLocalDataSource
    @Inject
    constructor(
        @UrlMultiAnalysisDsState state: LocalDataSourceState,
        private val urlAnalysisDao: UrlAnalysisDao,
        private val multiAnalysisDao: UrlMultiAnalysisDao
    ) : RelationalLocalDataSource<UrlMultiAnalysis>(
            state,
            queueDao = multiAnalysisDao
        ) {
        /**
         * Saves a list of [UrlMultiAnalysis] and their related analyses to the local database.
         *
         * @param parentId The parent entity ID, or `null` if not applicable.
         * @param models The list of [UrlMultiAnalysis] to save.
         */
        override suspend fun save(
            parentId: String?,
            models: List<UrlMultiAnalysis>
        ) {
            val (analysisEntities, multiAnalysisEntities) = convertToEntities(parentId, models)
            multiAnalysisDao.add(*multiAnalysisEntities)
            urlAnalysisDao.add(*analysisEntities)

            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(
                    LOG_TAG,
                    "A total of ${multiAnalysisEntities.size} ${UrlMultiAnalysis::class.java.simpleName} were added."
                )
            }

            models.forEach { m ->
                Debugging.logVerboseUrlMultiAnalysis(
                    LOG_TAG,
                    contextMsg = "${UrlMultiAnalysis::class.java.simpleName} added.",
                    m
                )
            }
        }

        /**
         * Updates a list of [UrlMultiAnalysis] and their related analyses in the local database.
         *
         * @param parentId The parent entity ID, or `null` if not applicable.
         * @param models The list of [UrlMultiAnalysis] to update.
         */
        override suspend fun update(
            parentId: String?,
            models: List<UrlMultiAnalysis>
        ) {
            val (analysisEntities, multiAnalysisEntities) = convertToEntities(parentId, models)
            multiAnalysisDao.update(*multiAnalysisEntities)
            urlAnalysisDao.update(*analysisEntities)

            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(
                    LOG_TAG,
                    "A total of ${multiAnalysisEntities.size} ${UrlMultiAnalysis::class.java.simpleName} were updated."
                )
            }

            models.forEach { m ->
                Debugging.logVerboseUrlMultiAnalysis(
                    LOG_TAG,
                    contextMsg = "${UrlMultiAnalysis::class.java.simpleName} updated.",
                    m
                )
            }
        }

        /**
         * Retrieves a [UrlMultiAnalysis] by its ID from the local database.
         *
         * @param id The unique identifier of the [UrlMultiAnalysis] to retrieve.
         * @return [Outcome.Success] with the found [UrlMultiAnalysis], or [Outcome.Failure] if not found.
         */
        override suspend fun getById(id: String): Outcome<UrlMultiAnalysis> {
            if (!multiAnalysisDao.exists(id)) {
                return Outcome.Failure(RepositoryError.NotFound)
            }

            val multiReputationWithReputations = multiAnalysisDao.getById(id)
            val multiReputation = multiReputationWithReputations.buildMultiAnalysis()
            return Outcome.Success(multiReputation)
        }

        /**
         * Retrieves a list of [UrlMultiAnalysis] by their IDs, including their related analyses.
         *
         * @param ids The IDs of the [UrlMultiAnalysis] to retrieve.
         * @return A list of [UrlMultiAnalysis] records.
         */
        override suspend fun getManyByIds(vararg ids: String): List<UrlMultiAnalysis> {
            val multiAnalysisWithAnalyses = multiAnalysisDao.getManyByIds(*ids)
            return multiAnalysisWithAnalyses.map { m -> m.buildMultiAnalysis() }
        }

        /**
         * Retrieves a paginated list of [UrlMultiAnalysis] records with their related analyses.
         *
         * @param page The page number (zero-based).
         * @param size The number of items per page.
         * @return A list of [UrlMultiAnalysis] records for the specified page.
         */
        override suspend fun getMany(
            page: Int,
            size: Int
        ): List<UrlMultiAnalysis> {
            val multiAnalysisWithAnalyses = multiAnalysisDao.getMany(page, size)
            return multiAnalysisWithAnalyses.map { m -> m.buildMultiAnalysis() }
        }

        /**
         * Checks if a [UrlMultiAnalysis] exists in the local database.
         *
         * @param model The [UrlMultiAnalysis] to check.
         * @return `true` if the entity exists, `false` otherwise.
         */
        override suspend fun exists(model: UrlMultiAnalysis): Boolean =
            multiAnalysisDao.exists(model.id)

        /**
         * Converts models to their corresponding [UrlAnalysisEntity] and [UrlMultiAnalysisEntity] arrays.
         *
         * @param parentId The parent entity ID, or `null` if not applicable.
         * @param models The list of [UrlMultiAnalysis] to convert.
         * @return A pair of arrays: [UrlAnalysisEntity] and [UrlMultiAnalysisEntity].
         */
        private fun convertToEntities(
            parentId: String?,
            models: List<UrlMultiAnalysis>
        ): Pair<Array<UrlAnalysisEntity>, Array<UrlMultiAnalysisEntity>> =
            Pair(
                models
                    .map { m ->
                        m.analyses
                            .map { a ->
                                UrlAnalysisEntity.createFromModel(a, m.id)
                            }
                    }.flatten()
                    .toTypedArray(),
                models
                    .map { m ->
                        UrlMultiAnalysisEntity.createFromModel(
                            model = m,
                            messageAnalysisId = parentId
                        )
                    }.toTypedArray()
            )

        private companion object {
            private val LOG_TAG = UrlMultiAnalysesLocalDataSource::class.java.simpleName
        }
    }