package com.openlysis.data.analysis.core.di

import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.repository.DefaultAnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.source.AnalysesLocalDataSource
import com.openlysis.data.analysis.core.source.AnalysesRemoteDataSource
import com.openlysis.data.analysis.model.message.MessageAnalysis
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
    @EmailAnalysesRepository
    @Singleton
    @Provides
    fun provideEmailAnalysesRepository(
        localDs: AnalysesLocalDataSource<MessageAnalysis>,
        @EmailAnalysesRemoteDataSource remoteDs:
            AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis>
    ): AnalysesRepository<AnalyzeMessage, MessageAnalysis> =
        DefaultAnalysesRepository(
            localDs = localDs,
            remoteDs = remoteDs
        )

    @SmsAnalysesRepository
    @Singleton
    @Provides
    fun provideSmsAnalysesRepository(
        localDs: AnalysesLocalDataSource<MessageAnalysis>,
        @SmsAnalysesRemoteDataSource remoteDs:
            AnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis>
    ): AnalysesRepository<AnalyzeMessage, MessageAnalysis> =
        DefaultAnalysesRepository(
            localDs = localDs,
            remoteDs = remoteDs
        )
}