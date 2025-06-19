package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.database.dao.QueueDao
import com.openlysis.data.local.LocalRepository

/**
 * Abstract base class for local data sources that manage entities in the local database.
 *
 * @param TModel The type of model managed by this data source.
 * @property state The [LocalDataSourceState] tracking entity limits.
 * @property queueDao The [QueueDao] for queue-like deletion operations.
 *
 * Implements [LocalRepository] for basic save and update operations, and enforces entity limit logic.
 */
internal abstract class LocalDataSource<TModel>(
    private val state: LocalDataSourceState,
    private val queueDao: QueueDao
) : LocalRepository<TModel>
    where TModel : Any {
    /**
     * Saves a model to the local database, deleting the oldest entity if the limit is reached.
     *
     * @param model The model to save.
     */
    override suspend fun save(model: TModel) {
        if (exists(model)) {
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
        if (!exists(model)) {
            Log.w(
                LOG_TAG,
                "Trying to update an entity that does not exist."
            )
            return
        }

        handleUpdate(model)
    }

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
     * Checks if a model already exists in the local database.
     *
     * @param model The model to check.
     * @return `true` if the model exists, `false` otherwise.
     */
    internal abstract suspend fun exists(model: TModel): Boolean

    companion object {
        private val LOG_TAG = LocalDataSource::class.java.simpleName
    }
}