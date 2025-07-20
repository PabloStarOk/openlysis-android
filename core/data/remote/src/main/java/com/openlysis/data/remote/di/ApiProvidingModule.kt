package com.openlysis.data.remote.di

import com.openlysis.data.analysis.core.di.EmailAnalysesRemoteDataSource
import com.openlysis.data.analysis.core.di.SmsAnalysesRemoteDataSource
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.source.AnalysesRemoteDataSource
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.dto.common.AnalysisType
import com.openlysis.data.remote.interceptor.ApiKeyHeaderInterceptor
import com.openlysis.data.remote.source.MessageAnalysesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun provideOpenlysisService(
        apiClientSettings: ApiClientSettings,
        apiKeyInterceptor: ApiKeyHeaderInterceptor
    ): OpenlysisApi {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(apiKeyInterceptor)
                .build()

        return Retrofit
            .Builder()
            .baseUrl(apiClientSettings.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(OpenlysisApi::class.java)
    }

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