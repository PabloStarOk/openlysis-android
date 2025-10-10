package com.openlysis.data.remote.dto.request

import com.squareup.moshi.JsonClass

/**
 * Data transfer object for sign-in refresh requests.
 *
 * @property refreshToken The refresh token used to obtain new access tokens.
 */
@JsonClass(generateAdapter = true)
data class SignInRefreshRequest(
    val refreshToken: String
)