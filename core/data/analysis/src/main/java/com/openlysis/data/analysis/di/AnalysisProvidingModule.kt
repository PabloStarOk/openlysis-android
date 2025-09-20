package com.openlysis.data.analysis.di

import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.repository.DefaultAnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.source.AnalysesLocalDataSource
import com.openlysis.data.analysis.source.AnalysesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies for analysis-related components.
 * This module is installed in the SingletonComponent, ensuring singleton scope for all provided dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AnalysisProvidingModule {
    @MessageAnalysesRepository(MessageType.Email)
    @Singleton
    @Provides
    fun provideEmailAnalysesRepository(
        @EmailAnalysesLocalDataSource localDs: AnalysesLocalDataSource<MessageAnalysis>,
        @EmailAnalysesRemoteDataSource remoteDs:
            AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis>
    ): AnalysesRepository<AnalyzeMessage, MessageAnalysis> =
        DefaultAnalysesRepository(
            localDs = localDs,
            remoteDs = remoteDs
        )

    @MessageAnalysesRepository(MessageType.Sms)
    @Singleton
    @Provides
    fun provideSmsAnalysesRepository(
        @SmsAnalysesLocalDataSource localDs: AnalysesLocalDataSource<MessageAnalysis>,
        @SmsAnalysesRemoteDataSource remoteDs:
            AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis>
    ): AnalysesRepository<AnalyzeMessage, MessageAnalysis> =
        DefaultAnalysesRepository(
            localDs = localDs,
            remoteDs = remoteDs
        )
}