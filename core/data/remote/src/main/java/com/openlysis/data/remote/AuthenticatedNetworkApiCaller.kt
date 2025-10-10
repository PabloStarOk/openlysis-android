package com.openlysis.data.remote

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.AuthTokensManager
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response

/**
 * Abstract base class for network API callers that require authentication.
 *
 * Ensures that authentication tokens are valid before making API calls.
 *
 * @property authTokensManager Manages authentication tokens and their validity.
 * @param dispatcher Coroutine dispatcher for network operations.
 */
internal abstract class AuthenticatedNetworkApiCaller(
    private val authTokensManager: AuthTokensManager,
    dispatcher: CoroutineDispatcher
) : NetworkApiCaller(dispatcher) {
    override suspend fun <TResult : Any> callApiSafely(
        apiCall: suspend () -> Response<TResult>
    ): Outcome<TResult> {
        val outcome = authTokensManager.ensureTokensValidity()
        if (outcome is Outcome.Failure) {
            return Outcome.Failure(outcome.error)
        }

        return super.callApiSafely(apiCall)
    }
}