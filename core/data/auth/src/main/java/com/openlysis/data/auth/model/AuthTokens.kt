package com.openlysis.data.auth.model

/**
 * Authentication tokens required for user's authentication with the back-end API.
 *
 * @property accessToken The access token for API authentication.
 * @property refreshToken The refresh token for obtaining new access tokens.
 */
data class AuthTokens(
    val accessToken: Token?,
    val refreshToken: Token?
) {
    /**
     * Returns `true` if the refresh token exists and is not expired.
     */
    val canRefresh get() = refreshToken?.isExpired == false

    /**
     * Indicates if the access token has expired and should be refreshed.
     */
    val shouldRefresh get() = accessToken?.isExpired == true
}