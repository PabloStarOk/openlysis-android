package com.openlysis.data.auth

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.model.AuthTokens
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages user authentication tokens, providing access to the latest tokens,
 * saving new tokens, and ensuring token validity.
 */
interface AuthTokensManager {
    /**
     * A [StateFlow] that emits the latest user authentication tokens.
     */
    val data: StateFlow<AuthTokens>

    /**
     * Indicates whether the authentication tokens have been loaded.
     */
    val isLoaded: Boolean

    /**
     * Saves the provided authentication tokens.
     *
     * @param authTokens The authentication tokens to be saved.
     */
    suspend fun saveTokens(authTokens: AuthTokens)

    /**
     * Ensures that the current authentication tokens are valid.
     *
     * @return [Outcome] indicating success or failure if an error occurs while ensuring.
     */
    suspend fun ensureTokensValidity(): Outcome<Unit>

    /**
     * Deletes the current authentication tokens which causes the user is not authenticated anymore.
     */
    suspend fun deleteTokens()
}