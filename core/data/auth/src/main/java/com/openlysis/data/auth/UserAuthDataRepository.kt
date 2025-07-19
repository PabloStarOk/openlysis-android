package com.openlysis.data.auth

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing user authentication data.
 */
interface UserAuthDataRepository {
    /**
     * A flow emitting the current user authentication data.
     */
    val data: Flow<UserAuthData>

    /**
     * Sets the signed-in state of the user.
     *
     * @param signedIn True if the user is signed in, false otherwise.
     */
    suspend fun setSignedIn(signedIn: Boolean)

    /**
     * Saves the provided API key for the user.
     *
     * @param apiKey The API key to save.
     */
    suspend fun saveApiKey(apiKey: String)

    /**
     * Deletes the stored API key for the user.
     */
    suspend fun deleteApiKey()
}