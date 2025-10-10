package com.openlysis.data.auth

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.core.outcome.NetworkError
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.model.AuthTokens
import com.openlysis.data.auth.model.Token
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Default implementation of [AuthTokensManager].
 *
 * Manages authentication tokens, handles token refresh logic,
 * and observes token expiration events.
 *
 * @property appScope Coroutine scope for launching flows and coroutines.
 * @property tokenExpirationWatcher Watches for refresh token expiration.
 * @property userAuthenticator Handles user authentication and token refresh.
 * @property localDataSource Local data source for storing authentication tokens.
 */
internal class DefaultAuthTokensManager
    @Inject
    constructor(
        @ApplicationScope private val appScope: CoroutineScope,
        private val tokenExpirationWatcher: TokenExpirationWatcher,
        private val userAuthenticator: UserAuthenticator,
        private val localDataSource: AuthTokensLocalDataSource
    ) : AuthTokensManager {
        private val refreshMutex = Mutex()

        override val data: StateFlow<AuthTokens> =
            localDataSource.data
                .onStart { watchRefreshTokenExpiration() }
                .onEach { _isLoaded = true }
                .stateIn(
                    scope = appScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = AuthTokens(accessToken = null, refreshToken = null)
                )

        private var _isLoaded = false
        override val isLoaded: Boolean get() = _isLoaded

        override suspend fun saveTokens(authTokens: AuthTokens) =
            localDataSource.saveTokens(authTokens)

        override suspend fun ensureTokensValidity(): Outcome<Unit> =
            refreshMutex.withLock {
                val authTokens = data.first { it.accessToken != null }
                if (!authTokens.shouldRefresh) {
                    return Outcome.Success(Unit)
                }

                if (!authTokens.canRefresh) {
                    invalidateTokens()
                    return Outcome.Failure(NetworkError.AccessDenied)
                }

                val outcome = userAuthenticator.refresh(authTokens.refreshToken as Token)
                return when (outcome) {
                    is Outcome.Success -> {
                        val newAuthTokens = outcome.value
                        saveTokens(newAuthTokens)
                        Outcome.Success(Unit)
                    }
                    is Outcome.Failure -> {
                        if (outcome.error is NetworkError.AccessDenied) invalidateTokens()
                        Outcome.Failure(outcome.error)
                    }
                }
            }

        override suspend fun deleteTokens() {
            localDataSource.deleteTokens()
        }

        private suspend fun invalidateTokens() = deleteTokens()

        private fun watchRefreshTokenExpiration() {
            localDataSource.data
                .onEach {
                    val refreshToken = it.refreshToken
                    if (refreshToken == null) return@onEach

                    tokenExpirationWatcher.watch(refreshToken) { invalidateTokens() }
                }.launchIn(appScope)
        }
    }