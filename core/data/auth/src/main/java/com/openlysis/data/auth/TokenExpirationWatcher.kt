package com.openlysis.data.auth

import com.openlysis.data.auth.model.Token

/**
 * Watches for the expiration of a given [Token].
 */
internal interface TokenExpirationWatcher {
    /**
     * Starts watching the specified [token] for expiration.
     *
     * @param token The token to monitor.
     * @param onExpired The callback to execute when the token expires.
     */
    suspend fun watch(
        token: Token,
        onExpired: suspend () -> Unit
    )
}