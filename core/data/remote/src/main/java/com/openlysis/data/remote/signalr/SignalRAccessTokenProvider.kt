package com.openlysis.data.remote.signalr

import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.AuthTokensManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Provides access tokens for SignalR connections.
 *
 * @property authTokensManager Manages authentication tokens and their validity.
 * @property ioDispatcher Coroutine dispatcher for IO operations.
 */
internal class SignalRAccessTokenProvider
    @Inject
    constructor(
        private val authTokensManager: AuthTokensManager,
        @Dispatcher(AppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
    ) {
        suspend fun provide(): String =
            withContext(ioDispatcher) {
                val outcome = authTokensManager.ensureTokensValidity()
                return@withContext when (outcome) {
                    is Outcome.Success -> {
                        val authTokens = authTokensManager.data.first { it.accessToken != null }
                        authTokens.accessToken?.value as String
                    }
                    is Outcome.Failure -> throw IOException(
                        "Error while refreshing token: ${outcome.error}"
                    )
                }
            }
    }