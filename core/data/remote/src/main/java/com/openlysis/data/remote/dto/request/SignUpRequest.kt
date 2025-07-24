package com.openlysis.data.remote.dto.request

import com.squareup.moshi.JsonClass

/**
 * Data transfer object for user sign-up requests.
 *
 * @property userName The user's chosen username.
 * @property email The user's email address.
 * @property password The user's password.
 */
@JsonClass(generateAdapter = true)
internal data class SignUpRequest(
    val userName: String,
    val email: String,
    val password: String
)