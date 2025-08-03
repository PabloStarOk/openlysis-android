package com.openlysis.data.user

import com.openlysis.data.user.model.UserData
import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for user data operations.
 */
interface UserDataLocalDataSource {
    /**
     * A [Flow] emitting the current [UserData].
     */
    val data: Flow<UserData>

    /**
     * Adds or updates the record of whether a permission has been asked and granted.
     *
     * @param permission The name of the permission.
     * @param granted Whether the permission was granted by the user.
     */
    suspend fun addOrUpdateAskedPermission(
        permission: String,
        granted: Boolean
    )
}