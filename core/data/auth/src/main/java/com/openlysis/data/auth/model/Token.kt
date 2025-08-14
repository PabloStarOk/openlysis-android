package com.openlysis.data.auth.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * An authentication token.
 *
 * @property value The token string.
 * @property expiresAt The expiration time of the token.
 */
data class Token(
    val value: String,
    val expiresAt: Instant
) {
    /**
     * Indicates whether the token is expired.
     */
    val isExpired: Boolean get() = Clock.System.now() >= expiresAt
}