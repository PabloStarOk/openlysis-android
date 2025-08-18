package com.openlysis.feature.auth.model

import androidx.annotation.Keep

/**
 * Represents the type of authentication action.
 *
 * - SignIn: User is signing in.
 * - SignUp: User is registering a new account.
 */
@Keep
enum class AuthenticationType {
    SignIn,
    SignUp
}