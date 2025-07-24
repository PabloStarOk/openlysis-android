package com.openlysis.data.auth

import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for user authentication data.
 */
interface UserAuthLocalDataSource {
    /**
     * A Flow emitting the current [UserAuthData].
     */
    val data: Flow<UserAuthData>

    /**
     * Sets the signed-in state of the user.
     *
     * @param signedIn `true` if the user is signed in, `false` otherwise.
     */
    suspend fun setSignedIn(signedIn: Boolean)

    /**
     * Saves the provided API key.
     *
     * @param apiKey The API key.
     */
    suspend fun saveApiKey(apiKey: String)

    /**
     * Deletes the stored API key.
     */
    suspend fun deleteApiKey()
}