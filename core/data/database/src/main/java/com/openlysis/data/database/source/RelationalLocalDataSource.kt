package com.openlysis.data.database.source

import com.openlysis.data.analysis.core.error.Outcome
import com.openlysis.data.analysis.core.error.RepositoryError
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.database.dao.QueueDao
import com.openlysis.data.database.dao.RetrievalDao

/**
 * Abstract base class for data sources that manage entities with parent-child relationships in the local database.
 *
 * Extends [LocalDataSource] and provides abstract methods for saving, updating, and retrieving entities by parent ID.
 *
 * @param TModel The type of model managed by this data source.
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param queueDao The [QueueDao] for queue-like deletion operations.
 */
internal abstract class RelationalLocalDataSource<TModel>(
    state: LocalDataSourceState,
    queueDao: QueueDao,
    private val retrievalDao: RetrievalDao<TModel>
) : LocalDataSource<TModel>(state, queueDao)
    where TModel : Model {
    /**
     * Saves a list of models with an optional parent ID.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of models to save.
     */
    internal abstract suspend fun save(
        parentId: String?,
        models: List<TModel>
    )

    /**
     * Updates a list of models with an optional parent ID.
     *
     * @param parentId The parent entity ID, or `null` if not applicable.
     * @param models The list of models to update.
     */
    internal abstract suspend fun update(
        parentId: String?,
        models: List<TModel>
    )

    override suspend fun handleSave(model: TModel) {
        save(
            parentId = null,
            listOf(model)
        )
    }

    override suspend fun handleUpdate(model: TModel) {
        update(
            parentId = null,
            listOf(model)
        )
    }

    override suspend fun getById(id: String): Outcome<TModel> {
        if (!retrievalDao.exists(id)) {
            return Outcome.Failure(RepositoryError.NotFound)
        }

        val multiReputationWithReputations = retrievalDao.getById(id)
        val multiReputation = multiReputationWithReputations.buildModel()
        return Outcome.Success(multiReputation)
    }

    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<TModel> {
        val multiAnalysisWithAnalyses = retrievalDao.getMany(page, size)
        return multiAnalysisWithAnalyses.map { m -> m.buildModel() }
    }

    /**
     * Retrieves a list of models by their IDs.
     *
     * @param ids The IDs of the entities to retrieve.
     * @return A list of models corresponding to the provided IDs.
     */
    suspend fun getManyByIds(vararg ids: String): List<TModel> {
        val pojoObjects = retrievalDao.getManyByIds(*ids)
        return pojoObjects.map { p -> p.buildModel() }
    }
}