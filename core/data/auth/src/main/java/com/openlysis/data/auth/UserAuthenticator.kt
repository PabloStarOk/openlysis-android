package com.openlysis.data.auth

import com.openlysis.core.outcome.Outcome

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
     * @return [Outcome] containing an API Key string on success.
     */
    suspend fun signIn(
        email: String,
        password: String
    ): Outcome<String>
}