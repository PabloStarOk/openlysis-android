package com.openlysis.data.remote.di

import com.openlysis.data.analysis.core.di.EmailAnalysesRemoteDataSource
import com.openlysis.data.analysis.core.di.SmsAnalysesRemoteDataSource
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.source.AnalysesRemoteDataSource
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.data.remote.AuthenticationApi
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.dto.common.AnalysisType
import com.openlysis.data.remote.interceptor.ApiKeyHeaderInterceptor
import com.openlysis.data.remote.source.MessageAnalysesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides API-related dependencies for remote data access.
 *
 * This module is installed in the [SingletonComponent] and is responsible for creating
 * and providing the [OpenlysisApi] Retrofit interface, configured with authentication
 * and base URL settings. It also provides different implementations for messages analyses
 * data sources.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object ApiProvidingModule {
    @Singleton
    @Provides
    fun provideHttpClient(apiKeyInterceptor: ApiKeyHeaderInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideOpenlysisApi(
        apiClientSettings: ApiClientSettings,
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): OpenlysisApi =
        Retrofit
            .Builder()
            .baseUrl(apiClientSettings.baseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(OpenlysisApi::class.java)

    @Singleton
    @Provides
    fun provideAuthenticationApi(
        apiClientSettings: ApiClientSettings,
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): AuthenticationApi =
        Retrofit
            .Builder()
            .baseUrl(apiClientSettings.baseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(AuthenticationApi::class.java)

    @EmailAnalysesRemoteDataSource
    @Singleton
    @Provides
    fun provideEmailAnalysisRepo(
        api: OpenlysisApi
    ): AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis> =
        MessageAnalysesRemoteDataSource(
            analysisType = AnalysisType.Email,
            api = api
        )

    @SmsAnalysesRemoteDataSource
    @Singleton
    @Provides
    fun provideSmsAnalysisRepo(
        api: OpenlysisApi
    ): AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis> =
        MessageAnalysesRemoteDataSource(
            analysisType = AnalysisType.Sms,
            api = api
        )
}