package com.openlysis.data.auth

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.model.AuthTokens
import com.openlysis.data.auth.model.Token

/**
 * Interface for user authentication operations.
 */
interface UserAuthenticator {
    /**
     * Registers a new user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return [Outcome] indicating success or failure.
     */
    suspend fun signUp(
        email: String,
        password: String
    ): Outcome<Unit>

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return [Outcome] containing [AuthTokens] on success.
     */
    suspend fun signIn(
        email: String,
        password: String
    ): Outcome<AuthTokens>

    /**
     * Refreshes authentication tokens using the provided refresh token.
     *
     * @param refreshToken The token used to obtain new authentication tokens.
     * @return [Outcome] containing new [AuthTokens] on success.
     */
    suspend fun refresh(refreshToken: Token): Outcome<AuthTokens>
}