package com.openlysis.data.remote.dto.request

import com.squareup.moshi.JsonClass

/**
 * Data transfer object for sign-in requests.
 *
 * @property userName The user's username.
 * @property password The user's password.
 */
@JsonClass(generateAdapter = true)
internal data class SignInRequest(
    val userName: String,
    val password: String
)