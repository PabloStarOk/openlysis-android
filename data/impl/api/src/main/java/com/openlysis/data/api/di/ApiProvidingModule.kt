package com.openlysis.data.api.di

import com.openlysis.data.api.ApiClientSettings
import com.openlysis.data.api.ApiCredentials
import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.api.interceptor.ApiKeyHeaderInterceptor
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
 * and providing the [OpenlysisService] Retrofit interface, configured with authentication
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
    ): OpenlysisService {
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
            .create(OpenlysisService::class.java)
    }
}