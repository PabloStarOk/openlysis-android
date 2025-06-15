package com.openlysis.data.local.source

import android.util.Log
import com.openlysis.data.local.Debugging
import com.openlysis.data.local.dao.FileAnalysisDao
import com.openlysis.data.local.dao.FileMultiAnalysisDao
import com.openlysis.data.local.entity.analysis.FileAnalysisEntity
import com.openlysis.data.local.entity.analysis.FileMultiAnalysisEntity
import com.openlysis.models.analysis.FileMultiAnalysis

/**
 * Data source for managing [FileMultiAnalysis] entities and their related analyses in the local database.
 *
 * Handles saving, updating, and retrieving [FileMultiAnalysis] records, including their associated [FileAnalysisEntity] records.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param fileAnalysisDao The [FileAnalysisDao] for file analysis operations.
 * @param multiAnalysisDao The [FileMultiAnalysisDao] for multi-analysis operations.
 */
internal class FileMultiAnalysisDataSource(
    state: LocalDataSourceState,
    private val fileAnalysisDao: FileAnalysisDao,
    private val multiAnalysisDao: FileMultiAnalysisDao
) : RelationalLocalDataSource<FileMultiAnalysis>(
        state = state,
        queueDao = multiAnalysisDao
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
     * Retrieves a list of [FileMultiAnalysis] by their IDs, including their related analyses.
     *
     * @param ids The IDs of the [FileMultiAnalysis] to retrieve.
     * @return A list of [FileMultiAnalysis] records.
     */
    override suspend fun getManyByIds(vararg ids: String): List<FileMultiAnalysis> {
        val multiAnalysisWithAnalyses = multiAnalysisDao.getManyByIds(*ids)
        return multiAnalysisWithAnalyses.map { m -> m.buildMultiAnalysis() }
    }

    /**
     * Retrieves a paginated list of [FileMultiAnalysis] records with their related analyses.
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [FileMultiAnalysis] records for the specified page.
     */
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<FileMultiAnalysis> {
        val multiAnalysisWithAnalyses = multiAnalysisDao.getMany(page, size)
        return multiAnalysisWithAnalyses.map { m -> m.buildMultiAnalysis() }
    }

    /**
     * Checks if a [FileMultiAnalysis] exists in the local database.
     *
     * @param model The [FileMultiAnalysis] to check.
     * @return `true` if the entity exists, `false` otherwise.
     */
    override suspend fun exists(model: FileMultiAnalysis): Boolean =
        multiAnalysisDao.exists(model.id)

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
        private val LOG_TAG = FileMultiAnalysisDataSource::class.java.simpleName
    }
}