package com.openlysis.data.remote.di

import javax.inject.Qualifier

/**
 * Qualifier annotation for specifying which [AppHttpClient] to inject.
 *
 * @property httpClient The specific [AppHttpClient] instance to be injected.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class HttpClient(
    val httpClient: AppHttpClient
)