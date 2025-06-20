package com.openlysis.data.remote.di

import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.data.remote.ApiCredentials
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.interceptor.ApiKeyHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides API-related dependencies for remote data access.
 *
 * This module is installed in the [SingletonComponent] and is responsible for creating
 * and providing the [OpenlysisApi] Retrofit interface, configured with authentication
 * and base URL settings.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object ApiProvidingModule {
    @Singleton
    @Provides
    fun provideOpenlysisService(
        apiClientSettings: ApiClientSettings,
        apiCredentials: ApiCredentials
    ): OpenlysisApi {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(ApiKeyHeaderInterceptor(apiCredentials))
                .build()

        return Retrofit
            .Builder()
            .baseUrl(apiClientSettings.baseUrl)
            .client(client)
            .build()
            .create(OpenlysisApi::class.java)
    }
}