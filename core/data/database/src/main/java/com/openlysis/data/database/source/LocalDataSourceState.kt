package com.openlysis.data.database.source

/**
 * State holder for a [LocalDataSource], tracking the number of stored entities and the maximum allowed.
 *
 * @property maxStoredEntities The maximum number of entities allowed in the data source.
 * @property currentStoredEntities The current number of entities stored.
 * @constructor Creates a new [LocalDataSourceState] with the given limits.
 */
internal data class LocalDataSourceState(
    private val maxStoredEntities: Int,
    private var currentStoredEntities: Int
) {
    /**
     * Returns `true` if the maximum entity limit has been reached.
     */
    internal val isLimitReached
        get() = currentStoredEntities >= maxStoredEntities

    /**
     * Increments the count of stored entities by one.
     */
    internal fun countNewEntity() {
        currentStoredEntities += 1
    }
}