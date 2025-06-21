package com.openlysis.data.database.source

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
) : LocalDataSource<TModel>(
        state = state,
        queueDao = queueDao,
        existsDao = retrievalDao
    )
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

    /**
     * Handles saving a single model by delegating to [save] with a null parent ID.
     *
     * @param model The model instance to save.
     */
    override suspend fun handleSave(model: TModel) {
        save(
            parentId = null,
            listOf(model)
        )
    }

    /**
     * Handles updating a single model by delegating to [update] with a null parent ID.
     *
     * @param model The model instance to update.
     */
    override suspend fun handleUpdate(model: TModel) {
        update(
            parentId = null,
            listOf(model)
        )
    }

    /**
     * Retrieves a [TModel] by its unique ID.
     *
     * @param id The unique identifier of the [TModel] to retrieve.
     * @return The [TModel] object with all related analyses and reputations loaded.
     */
    override suspend fun handleGetById(id: String): TModel = retrievalDao.getById(id).buildModel()

    /**
     * Retrieves a paginated list of models.
     *
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return A list of models for the specified page and size.
     */
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