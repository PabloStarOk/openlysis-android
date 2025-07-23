package com.openlysis.feature.auth

/**
 * Represents the UI state for authentication screen.
 *
 * @property authType The type of authentication being used.
 * @property email The user's email input.
 * @property password The user's password input.
 * @property canAuthenticate Indicates if authentication can proceed.
 * @property authStatus The current authentication status.
 */
internal data class AuthUiState(
    val authType: AuthenticationType,
    val email: String = "",
    val password: String = "",
    val canAuthenticate: Boolean = false,
    val authStatus: AuthenticationStatus = AuthenticationStatus.None
)