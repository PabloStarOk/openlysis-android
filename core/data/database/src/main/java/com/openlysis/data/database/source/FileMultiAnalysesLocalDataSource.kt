package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.database.Debugging
import com.openlysis.data.database.dao.FileAnalysisDao
import com.openlysis.data.database.dao.FileMultiAnalysisDao
import com.openlysis.data.database.di.FileMultiAnalysisDsState
import com.openlysis.data.database.entity.analysis.FileAnalysisEntity
import com.openlysis.data.database.entity.analysis.FileMultiAnalysisEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for managing [FileMultiAnalysis] entities and their related analyses in the local database.
 *
 * Handles saving, updating, and retrieving [FileMultiAnalysis] records, including their associated [FileAnalysisEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param fileAnalysisDao The [FileAnalysisDao] for file analysis operations.
 * @param multiAnalysisDao The [FileMultiAnalysisDao] for multi-analysis operations.
 */
@Singleton
internal class FileMultiAnalysesLocalDataSource
    @Inject
    constructor(
        @FileMultiAnalysisDsState state: LocalDataSourceState,
        private val fileAnalysisDao: FileAnalysisDao,
        private val multiAnalysisDao: FileMultiAnalysisDao
    ) : RelationalLocalDataSource<FileMultiAnalysis>(
            state = state,
            queueDao = multiAnalysisDao,
            retrievalDao = multiAnalysisDao
        ) {
        /**
         * Saves a list of [FileMultiAnalysis] and their related analyses to the local database.
         *
         * @param parentId The parent entity ID, or `null` if not applicable.
         * @param models The list of [FileMultiAnalysis] to save.
         */
        override suspend fun save(
            parentId: String?,
            models: List<FileMultiAnalysis>
        ) {
            val (analysisEntities, multiAnalysisEntities) = convertToEntities(parentId, models)
            multiAnalysisDao.add(*multiAnalysisEntities)
            fileAnalysisDao.add(*analysisEntities)

            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(
                    LOG_TAG,
                    "A total of ${multiAnalysisEntities.size} ${FileMultiAnalysis::class.java.simpleName} were added."
                )
            }

            models.forEach { m ->
                Debugging.logVerboseFileMultiAnalysis(
                    LOG_TAG,
                    contextMsg = "${FileMultiAnalysis::class.java.simpleName} added.",
                    m
                )
            }
        }

        /**
         * Updates a list of [FileMultiAnalysis] and their related analyses in the local database.
         *
         * @param parentId The parent entity ID, or `null` if not applicable.
         * @param models The list of [FileMultiAnalysis] to update.
         */
        override suspend fun update(
            parentId: String?,
            models: List<FileMultiAnalysis>
        ) {
            val (analysisEntities, multiAnalysisEntities) = convertToEntities(parentId, models)
            multiAnalysisDao.update(*multiAnalysisEntities)
            fileAnalysisDao.update(*analysisEntities)

            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(
                    LOG_TAG,
                    "A total of ${multiAnalysisEntities.size} ${FileMultiAnalysis::class.java.simpleName} were updated."
                )
            }

            models.forEach { m ->
                Debugging.logVerboseFileMultiAnalysis(
                    LOG_TAG,
                    contextMsg = "${FileMultiAnalysis::class.java.simpleName} updated.",
                    m
                )
            }
        }

        /**
         * Converts models to their corresponding [FileAnalysisEntity] and [FileMultiAnalysisEntity] arrays.
         *
         * @param parentId The parent entity ID, or `null` if not applicable.
         * @param models The list of [FileMultiAnalysis] to convert.
         * @return A pair of arrays: [FileAnalysisEntity] and [FileMultiAnalysisEntity].
         */
        private fun convertToEntities(
            parentId: String?,
            models: List<FileMultiAnalysis>
        ): Pair<Array<FileAnalysisEntity>, Array<FileMultiAnalysisEntity>> =
            Pair(
                models
                    .map { m ->
                        m.analyses
                            .map { a ->
                                FileAnalysisEntity.createFromModel(a, m.id)
                            }
                    }.flatten()
                    .toTypedArray(),
                models
                    .map { m ->
                        FileMultiAnalysisEntity.createFromModel(
                            model = m,
                            messageAnalysisId = parentId
                        )
                    }.toTypedArray()
            )

        private companion object {
            private val LOG_TAG = FileMultiAnalysesLocalDataSource::class.java.simpleName
        }
    }