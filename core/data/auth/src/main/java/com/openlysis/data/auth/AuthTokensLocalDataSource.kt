package com.openlysis.data.auth

import com.openlysis.data.auth.model.AuthTokens
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for managing authentication tokens in local storage.
 */
interface AuthTokensLocalDataSource {
    /**
     * A Flow emitting the current stored [AuthTokens].
     */
    val data: Flow<AuthTokens>

    /**
     * Saves the provided [AuthTokens] to local storage.
     *
     * @param authTokens The authentication tokens to be saved.
     */
    suspend fun saveTokens(authTokens: AuthTokens)

    /**
     * Deletes the authentication tokens from local storage.
     */
    suspend fun deleteTokens()
}