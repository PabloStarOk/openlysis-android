package com.openlysis.data.remote.interceptor

import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.data.auth.UserAuthDataRepository
import com.openlysis.data.remote.constant.ApiHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An OkHttp interceptor that adds the API key header to every request.
 *
 * @property appScope The coroutine scope used for state management.
 * @property authDataRepository Repository providing user authentication data.
 */
internal class ApiKeyHeaderInterceptor
    @Inject
    constructor(
        @ApplicationScope private val appScope: CoroutineScope,
        authDataRepository: UserAuthDataRepository
    ) : Interceptor {
        private val apiKeyState =
            authDataRepository.data
                .map { it.apiKey.orEmpty() }
                .stateIn(
                    scope = appScope,
                    started = SharingStarted.Eagerly,
                    initialValue = ""
                )

        /**
         * Intercepts the outgoing request and adds the API key header.
         *
         * @param chain The OkHttp interceptor chain.
         * @return The HTTP response after adding the API key header.
         */
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val newRequest =
                originalRequest
                    .newBuilder()
                    .header(ApiHeaders.API_KEY, apiKeyState.value)
                    .build()
            return chain.proceed(newRequest)
        }
    }