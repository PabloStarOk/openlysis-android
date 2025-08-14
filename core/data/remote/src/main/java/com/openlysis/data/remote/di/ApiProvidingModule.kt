package com.openlysis.data.remote.di

import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.data.analysis.di.EmailAnalysesRemoteDataSource
import com.openlysis.data.analysis.di.SmsAnalysesRemoteDataSource
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.source.AnalysesRemoteDataSource
import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.data.remote.AuthenticationApi
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.dto.common.AnalysisType
import com.openlysis.data.remote.interceptor.AuthHeaderInterceptor
import com.openlysis.data.remote.source.MessageAnalysesRemoteDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
    @HttpClient(AppHttpClient.Analysis)
    @Singleton
    @Provides
    fun provideAnalysisHttpClient(authHeaderInterceptor: AuthHeaderInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(authHeaderInterceptor)
            .build()

    @HttpClient(AppHttpClient.Authentication)
    @Singleton
    @Provides
    fun provideAuthHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    fun provideAnalysisApi(
        apiClientSettings: ApiClientSettings,
        converterFactory: Converter.Factory,
        @HttpClient(AppHttpClient.Analysis) client: OkHttpClient
    ): OpenlysisApi =
        Retrofit
            .Builder()
            .baseUrl(apiClientSettings.analysisApiBaseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(OpenlysisApi::class.java)

    @Singleton
    @Provides
    fun provideAuthenticationApi(
        apiClientSettings: ApiClientSettings,
        converterFactory: Converter.Factory,
        @HttpClient(AppHttpClient.Authentication) client: OkHttpClient
    ): AuthenticationApi =
        Retrofit
            .Builder()
            .baseUrl(apiClientSettings.authApiBaseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()
            .create(AuthenticationApi::class.java)

    @EmailAnalysesRemoteDataSource
    @Singleton
    @Provides
    fun provideEmailAnalysisRepo(
        authTokensManager: AuthTokensManager,
        api: OpenlysisApi,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis> =
        MessageAnalysesRemoteDataSource(
            analysisType = AnalysisType.Email,
            authTokensManager = authTokensManager,
            api = api,
            dispatcher = ioDispatcher
        )

    @SmsAnalysesRemoteDataSource
    @Singleton
    @Provides
    fun provideSmsAnalysisRepo(
        authTokensManager: AuthTokensManager,
        api: OpenlysisApi,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis> =
        MessageAnalysesRemoteDataSource(
            analysisType = AnalysisType.Sms,
            authTokensManager = authTokensManager,
            api = api,
            dispatcher = ioDispatcher
        )
}