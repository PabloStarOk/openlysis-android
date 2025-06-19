package com.openlysis.domain.di

import com.openlysis.data.remote.AnalysisRepository
import com.openlysis.data.remote.request.AnalyzeFile
import com.openlysis.data.remote.request.AnalyzeMessage
import com.openlysis.data.remote.request.AnalyzeUrl
import com.openlysis.models.analysis.FileMultiAnalysis
import com.openlysis.models.analysis.UrlMultiAnalysis
import com.openlysis.models.message.MessageAnalysis
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dagger Hilt module for providing domain-level dependencies.
 * Binds implementations of [AnalysisRepository] for different analysis types.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DomainModule {
    @Binds
    abstract fun bindUrlMultiAnalysisRepo(
        abstraction: AnalysisRepository<AnalyzeUrl, UrlMultiAnalysis>
    ): AnalysisRepository<*, UrlMultiAnalysis>

    @Binds
    abstract fun bindFileMultiAnalysisRepo(
        abstraction: AnalysisRepository<AnalyzeFile, FileMultiAnalysis>
    ): AnalysisRepository<*, FileMultiAnalysis>

    @Binds
    abstract fun bindMessageAnalysisRepo(
        abstraction: AnalysisRepository<AnalyzeMessage, MessageAnalysis>
    ): AnalysisRepository<*, MessageAnalysis>
}