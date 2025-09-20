package com.openlysis.data.remote.di

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.messagepack.MessagePackHubProtocol
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.data.analysis.di.MessageAnalysisDependency
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeFile
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.request.AnalyzeUrl
import com.openlysis.data.analysis.service.AnalysisUpdateTracker
import com.openlysis.data.analysis.source.AnalysesRemoteDataSource
import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.remote.ApiClientSettings
import com.openlysis.data.remote.AuthenticationApi
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.dto.common.AnalysisType
import com.openlysis.data.remote.interceptor.AuthHeaderInterceptor
import com.openlysis.data.remote.signalr.DefaultSignalRConnectionProvider
import com.openlysis.data.remote.signalr.SignalRAccessTokenProvider
import com.openlysis.data.remote.signalr.SignalRAnalysisUpdateTracker
import com.openlysis.data.remote.signalr.SignalRConnectionProvider
import com.openlysis.data.remote.signalr.SignalRHubMethod
import com.openlysis.data.remote.signalr.dto.FileMultiAnalysisDto
import com.openlysis.data.remote.signalr.dto.MessageAnalysisDto
import com.openlysis.data.remote.signalr.dto.UrlMultiAnalysisDto
import com.openlysis.data.remote.source.MessageAnalysesRemoteDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.rx3.rxSingle
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides API-related dependencies for remote data access.
 *
 * This module is installed in the [SingletonComponent] and is responsible for creating
 * and providing the [OpenlysisApi] Retrofit interface, configured with authentication
 * and base URL settings. It also provides different implementations for messages analyses
 * data sources and analysis update trackers.
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
    fun provideHubConnection(
        apiClientSettings: ApiClientSettings,
        accessTokenProvider: SignalRAccessTokenProvider
    ): HubConnection =
        HubConnectionBuilder
            .create(apiClientSettings.analysisUpdatesSignalRHubUrl.toString())
            .withAccessTokenProvider(rxSingle { accessTokenProvider.provide() })
            .withHubProtocol(MessagePackHubProtocol())
            .withServerTimeout(Duration.ofSeconds(30).toMillis())
            .build()

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

    @MessageAnalysisDependency(MessageType.Email)
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

    @MessageAnalysisDependency(MessageType.Sms)
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

    @Singleton
    @Provides
    fun provideSignalRConnectionProvider(
        hubConnection: HubConnection,
        @ApplicationScope appScope: CoroutineScope,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): SignalRConnectionProvider {
        val provider =
            DefaultSignalRConnectionProvider(
                stopDelayMillis = 10_000,
                hubConnection,
                appScope,
                ioDispatcher
            )
        provider.listenForConnectionEvents()
        return provider
    }

    @MessageAnalysisDependency(MessageType.Email)
    @Singleton
    @Provides
    fun provideEmailAnalysisUpdateTracker(
        connectionProvider: SignalRConnectionProvider,
        @MessageAnalysisDependency(MessageType.Email)
        repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ): AnalysisUpdateTracker<MessageAnalysis> =
        SignalRAnalysisUpdateTracker(
            appScope,
            connectionProvider,
            hubMethod = SignalRHubMethod.ReceiveEmailAnalysisUpdate,
            dtoClass = MessageAnalysisDto::class,
            repository = repository,
            isAnalysisUpdatableCallback = {
                it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress
            }
        )

    @MessageAnalysisDependency(MessageType.Sms)
    @Singleton
    @Provides
    fun provideSmsAnalysisUpdateTracker(
        connectionProvider: SignalRConnectionProvider,
        @MessageAnalysisDependency(MessageType.Sms)
        repository: AnalysesRepository<AnalyzeMessage, MessageAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ): AnalysisUpdateTracker<MessageAnalysis> =
        SignalRAnalysisUpdateTracker(
            appScope,
            connectionProvider,
            hubMethod = SignalRHubMethod.ReceiveSmsAnalysisUpdate,
            dtoClass = MessageAnalysisDto::class,
            repository = repository,
            isAnalysisUpdatableCallback = {
                it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress
            }
        )

    @Singleton
    @Provides
    fun provideFileMultiAnalysisUpdateTracker(
        connectionProvider: SignalRConnectionProvider,
        repository: AnalysesRepository<AnalyzeFile, FileMultiAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ): AnalysisUpdateTracker<FileMultiAnalysis> =
        SignalRAnalysisUpdateTracker(
            appScope,
            connectionProvider,
            hubMethod = SignalRHubMethod.ReceiveFileMultiAnalysisUpdate,
            dtoClass = FileMultiAnalysisDto::class,
            repository = repository,
            isAnalysisUpdatableCallback = {
                it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress
            }
        )

    @Singleton
    @Provides
    fun provideUrlMultiAnalysisUpdateTracker(
        connectionProvider: SignalRConnectionProvider,
        repository: AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>,
        @ApplicationScope appScope: CoroutineScope
    ): AnalysisUpdateTracker<UrlMultiAnalysis> =
        SignalRAnalysisUpdateTracker(
            appScope,
            connectionProvider,
            hubMethod = SignalRHubMethod.ReceiveUrlMultiAnalysisUpdate,
            dtoClass = UrlMultiAnalysisDto::class,
            repository = repository,
            isAnalysisUpdatableCallback = {
                it.status == AnalysisStatus.Queued || it.status == AnalysisStatus.InProgress
            }
        )
}