package com.openlysis.data.remote.jwt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Payload of a JWT.
 *
 * @property expiration The expiration time of the token in seconds since epoch.
 */
@JsonClass(generateAdapter = true)
internal data class JwtPayload(
    @Json(name = "exp")
    val expiration: Long
)