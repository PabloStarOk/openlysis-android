package com.openlysis.data.remote.dto.authentication

import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object representing authentication tokens.
 *
 * @property accessToken The access token for API authentication.
 * @property refreshToken The refresh token used to obtain new access tokens.
 * @property refreshTokenExpiration The expiration date/time of the refresh token.
 */
@JsonClass(generateAdapter = true)
internal data class AuthTokensDto(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpiration: String
)