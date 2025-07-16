package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.analysis.core.error.RepositoryError
import com.openlysis.data.analysis.core.source.AnalysesLocalDataSource
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Outcome
import com.openlysis.data.database.dao.ExistsDao
import com.openlysis.data.database.dao.QueueDao

/**
 * Abstract base class for local data sources that manage entities in the local database.
 *
 * Implements [AnalysesLocalDataSource] for save and update operations, and enforces entity limit logic.
 *
 * @param TModel The type of model managed by this data source.
 * @property state The [LocalDataSourceState] tracking entity limits.
 * @property queueDao The [QueueDao] for queue-like deletion operations.
 * @property existsDao The [ExistsDao] used to check for the existence of entities.
 */
internal abstract class LocalDataSource<TModel>(
    private val state: LocalDataSourceState,
    private val queueDao: QueueDao,
    private val existsDao: ExistsDao
) : AnalysesLocalDataSource<TModel>
    where TModel : Model {
    /**
     * Saves a model to the local database, deleting the oldest entity if the limit is reached.
     *
     * @param model The model to save.
     */
    override suspend fun save(model: TModel) {
        if (existsDao.exists(model.id)) {
            Log.e(
                LOG_TAG,
                "Trying to add an entity that already exists."
            )
            return
        }

        if (state.isLimitReached) {
            if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
                Log.d(
                    LOG_TAG,
                    "Deleting oldest entity."
                )
            }

            queueDao.deleteOldest()
        }

        handleSave(model)
        state.countNewEntity()
    }

    /**
     * Updates a model in the local database.
     *
     * @param model The model to update.
     */
    override suspend fun update(model: TModel) {
        if (!existsDao.exists(model.id)) {
            Log.w(
                LOG_TAG,
                "Trying to update an entity that does not exist."
            )
            return
        }

        handleUpdate(model)
    }

    /**
     * Retrieves a model by its ID.
     *
     * @param id The unique identifier of the model.
     * @return [Outcome] containing the model if found, or [Outcome.Failure] with [RepositoryError.NotFound] if not found.
     */
    override suspend fun getById(id: String): Outcome<TModel> {
        if (existsDao.exists(id)) {
            return Outcome.Success(handleGetById(id))
        }

        return Outcome.Failure(RepositoryError.NotFound)
    }

    /**
     * Retrieves a list of models from the local database based on pagination parameters.
     *
     * @param page The page number to retrieve (starting from 1).
     * @param size The number of models per page.
     * @return A list of models for the specified page.
     */
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<TModel> {
        val zeroBasedPage = page - 1
        return handleGetMany(zeroBasedPage, size)
    }

    /**
     * Retrieves a list of models from the local database based on pagination parameters.
     *
     * @param page The page number to retrieve (starting from 1).
     * @param size The number of models per page.
     * @return A list of models for the specified page.
     */
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<TModel> {
        val zeroBasedPage = page - 1
        return handleGetMany(zeroBasedPage, size)
    }

    /**
     * Checks if a model with the given ID exists in the local database.
     *
     * @param model The model whose existence is to be checked.
     * @return `true` if the model exists, `false` otherwise.
     */
    override suspend fun exists(model: TModel): Boolean = existsDao.exists(model.id)

    /**
     * Handles the actual save logic for the model. Must be implemented by subclasses.
     *
     * @param model The model to save.
     */
    internal abstract suspend fun handleSave(model: TModel)

    /**
     * Handles the actual update logic for the model. Must be implemented by subclasses.
     *
     * @param model The model to update.
     */
    internal abstract suspend fun handleUpdate(model: TModel)

    /**
     * Retrieves a model by its ID from the local database.
     *
     * @param id The unique identifier of the model to retrieve.
     * @return The model corresponding to the given ID.
     */
    internal abstract suspend fun handleGetById(id: String): TModel

    /**
     * Retrieves a list of models from the local database based on pagination parameters.
     *
     * @param page The page number to retrieve (starting from 0).
     * @param size The number of models per page.
     * @return A list of models for the specified page.
     */
    internal abstract suspend fun handleGetMany(
        page: Int,
        size: Int
    ): List<TModel>

    companion object {
        private val LOG_TAG = LocalDataSource::class.java.simpleName
    }
}