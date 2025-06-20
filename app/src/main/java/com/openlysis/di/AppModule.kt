package com.openlysis.di

import com.openlysis.BuildConfig
import com.openlysis.data.analysis.core.repository.AnalysesRepositorySettings
import com.openlysis.data.database.LocalStoragePreferences
import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.data.remote.ApiCredentials
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.net.URI
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-wide dependencies.
 * Installed in the SingletonComponent to ensure singleton instances.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Singleton
    @Provides
    fun provideApiClientSettings(): ApiClientSettings {
        val url =
            if (BuildConfig.API_BASE_URL.endsWith('/')) {
                BuildConfig.API_BASE_URL
            } else {
                "${BuildConfig.API_BASE_URL}/"
            }
        return ApiClientSettings(baseUrl = URI(url).toURL())
    }

    @Singleton
    @Provides
    fun provideAnalysesRepositorySettings(): AnalysesRepositorySettings =
        AnalysesRepositorySettings(paginationSize = 10)

    // TODO: Implement repository for encrypted API credentials.
    @Singleton
    @Provides
    fun provideApiCredentials(): ApiCredentials = ApiCredentials("TODO")

    // TODO: Implement repository for user preferences.
    @Singleton
    @Provides
    fun provideLocalDbSettings(): LocalStoragePreferences =
        LocalStoragePreferences(
            maxStoredUrlMultiAnalyses = 10,
            maxStoredFileMultiAnalyses = 10,
            maxStoredMessageAnalyses = 10,
            maxStoredEmailMultiReputations = 10,
            maxStoredPhoneMultiReputations = 10
        )
}