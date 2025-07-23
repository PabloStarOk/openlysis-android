package com.openlysis.feature.auth

import com.openlysis.core.outcome.AppError

/**
 * Represents the authentication status in the application.
 */
internal sealed interface AuthenticationStatus {
    /**
     * Indicates a successful authentication.
     */
    data object Success : AuthenticationStatus

    /**
     * Indicates a failed authentication with an associated error.
     * @property error The error that caused authentication to fail.
     */
    data class Failure(
        val error: AppError
    ) : AuthenticationStatus

    /**
     * Indicates that authentication is currently in progress.
     */
    data object InProgress : AuthenticationStatus

    /**
     * Indicates that no authentication state is currently set.
     */
    data object None : AuthenticationStatus
}