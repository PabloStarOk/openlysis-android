package com.openlysis.data.auth

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.auth.model.Token
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

/**
 * Default implementation of [TokenExpirationWatcher].
 *
 * Monitors token expiration using a coroutine scope and a clock.
 *
 * @property appScope Coroutine scope used to launch expiration watcher jobs.
 * @property clock Provides current time for expiration calculations.
 */
internal class DefaultTokenExpirationWatcher
    @Inject
    constructor(
        @ApplicationScope private val appScope: CoroutineScope,
        private val clock: Clock
    ) : TokenExpirationWatcher {
        private var currentWaitingJob: Job? = null
        private val secondSkew = 1_000

        override suspend fun watch(
            token: Token,
            onExpired: suspend () -> Unit
        ) {
            if (currentWaitingJob != null) {
                currentWaitingJob?.cancel()
            }

            val milliUntilExpiration =
                (token.expiresAt.toEpochMilliseconds() - clock.now().toEpochMilliseconds()) +
                    secondSkew

            if (milliUntilExpiration < 0) {
                onExpired()
                return
            }

            currentWaitingJob =
                appScope.launch {
                    delay(milliUntilExpiration)
                    onExpired()
                }
            currentWaitingJob?.invokeOnCompletion { currentWaitingJob = null }
        }
    }