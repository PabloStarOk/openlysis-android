package com.openlysis.data.remote.dto.authentication

import com.squareup.moshi.JsonClass

/**
 * Data transfer object representing the response for a sign-in request.
 *
 * @property apiKey The API key returned upon successful authentication.
 */
@JsonClass(generateAdapter = true)
internal data class SignInResponse(
    val apiKey: String
)