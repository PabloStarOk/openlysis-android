package com.openlysis.data.remote.interceptor

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.auth.model.Token
import com.openlysis.data.remote.constant.ApiHeaderPrefixes
import com.openlysis.data.remote.constant.ApiHeaders
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An OkHttp interceptor that adds the authorization header to every request.
 *
 * @property appScope The coroutine scope used for state management.
 * @property authDataRepository Repository providing user authentication tokens.
 */
internal class AuthHeaderInterceptor
    @Inject
    constructor(
        @ApplicationScope private val appScope: CoroutineScope,
        private val authDataRepository: AuthTokensManager
    ) : Interceptor {
        /**
         * Intercepts the outgoing request and adds the access token header.
         *
         * @param chain The OkHttp interceptor chain.
         * @return The HTTP response after adding the access token header.
         */
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val accessToken = authDataRepository.data.value.accessToken

            if (accessToken == null) return chain.proceed(originalRequest)

            val headerValue = formatHeaderValue(accessToken)
            val newRequest =
                originalRequest
                    .newBuilder()
                    .header(ApiHeaders.AUTHORIZATION, headerValue)
                    .build()
            return chain.proceed(newRequest)
        }

        private fun formatHeaderValue(accessToken: Token): String =
            "${ApiHeaderPrefixes.BEARER_TOKEN} ${accessToken.value}"
    }