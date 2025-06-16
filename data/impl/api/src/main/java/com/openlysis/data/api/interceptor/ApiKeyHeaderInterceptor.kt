package com.openlysis.data.api.interceptor

import com.openlysis.data.api.ApiCredentials
import com.openlysis.data.api.constant.ApiHeaders
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor that adds the API key header to every request.
 *
 * @property credentials The API credentials containing the API key.
 */
internal class ApiKeyHeaderInterceptor(
    val credentials: ApiCredentials
) : Interceptor {
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
                .header(ApiHeaders.API_KEY, credentials.apiKey)
                .build()
        return chain.proceed(newRequest)
    }
}