package com.openlysis.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

/**
 * Generic DAO interface for Room database operations on entities.
 *
 * @param TEntity The type of the entity.
 */
@Dao
internal interface EntityDao<TEntity>
    where TEntity : Any {
    /**
     * Inserts one or more entities into the database.
     *
     * @param entities The entities to insert.
     */
    @Insert
    suspend fun add(vararg entities: TEntity)

    /**
     * Updates one or more entities in the database.
     *
     * @param entities The entities to update.
     */
    @Update
    suspend fun update(vararg entities: TEntity)
}