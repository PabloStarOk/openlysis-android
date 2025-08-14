package com.openlysis.data.remote.dto.request

import com.squareup.moshi.JsonClass

/**
 * Data transfer object for sign-in requests.
 *
 * @property email The user's email address.
 * @property password The user's password.
 */
@JsonClass(generateAdapter = true)
internal data class SignInRequest(
    val email: String,
    val password: String
)