package com.openlysis.feature.auth

import androidx.compose.runtime.Immutable
import com.openlysis.feature.auth.model.AuthenticationStatus
import com.openlysis.feature.auth.model.AuthenticationType
import com.openlysis.feature.auth.model.PasswordRequirement
import com.openlysis.feature.auth.model.PasswordRequirementType

/**
 * Represents the UI state for authentication screen.
 *
 * @property authType The type of authentication being used.
 * @property email The user's email input.
 * @property password The user's password input.
 * @property confirmPassword The user's confirm password input.
 * @property canAuthenticate Indicates if authentication can proceed.
 * @property passwordRequirements List of password requirements and their satisfaction status.
 * @property passwordsMatch Indicates if password and confirmPassword match.
 * @property authStatus The current authentication status.
 */
@Immutable
internal data class AuthUiState(
    val authType: AuthenticationType,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val canAuthenticate: Boolean = false,
    val passwordRequirements: List<PasswordRequirement> =
        buildList {
            PasswordRequirementType.entries.forEach {
                add(PasswordRequirement(type = it, isSatisfied = false))
            }
        },
    val passwordsMatch: Boolean = password == confirmPassword,
    val authStatus: AuthenticationStatus = AuthenticationStatus.None
)