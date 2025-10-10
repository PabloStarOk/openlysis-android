package com.openlysis.di

import com.openlysis.BuildConfig
import com.openlysis.data.analysis.model.common.AnalysisSettings
import com.openlysis.data.database.LocalStoragePreferences
import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.feature.tools.model.FileAttachmentSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.net.URI
import java.net.URL
import java.util.Locale
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
    fun provideApiClientSettings(): ApiClientSettings =
        ApiClientSettings(
            analysisApiBaseUrl = getSafeUrl(BuildConfig.ANALYSIS_API_BASE_URL),
            authApiBaseUrl = getSafeUrl(BuildConfig.AUTH_API_BASE_URL),
            analysisUpdatesSignalRHubUrl = getSafeUrl(BuildConfig.ANALYSIS_UPDATES_SIGNALR_HUB_URL)
        )

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

    @Singleton
    @Provides
    fun provideFileAttachmentSettings(): FileAttachmentSettings =
        FileAttachmentSettings(
            maxFilesAmount = BuildConfig.MAX_ATTACHMENT_FILES,
            maxFileSize = BuildConfig.MAX_ATTACHMENT_FILE_SIZE_BYTES
        )

    // TODO: Implement country code detection (geolocation first, then telephony service, fallbacks to locale).
    // TODO: Configure reanalyze booleans from user preferences.
    @Singleton
    @Provides
    fun provideAnalysisSettings(): AnalysisSettings {
        val androidLocale = androidx.compose.ui.text.intl.Locale.current.platformLocale
        val countryIsoCode =
            if (androidLocale.country.length == 2) {
                androidLocale.country
            } else {
                Locale.getISOCountries().first()
            }
        return AnalysisSettings(
            defaultCountryCode = countryIsoCode,
            reanalyzeEmails = true,
            reanalyzeSms = true,
            reanalyzeUrls = true,
            reanalyzeFiles = true
        )
    }

    private fun getSafeUrl(input: String): URL {
        val sanitizedUrl = if (input.endsWith('/')) input else "$input/"
        return URI(sanitizedUrl).toURL()
    }
}