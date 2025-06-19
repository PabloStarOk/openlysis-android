package com.openlysis.domain.di

import com.openlysis.data.analysis.core.AnalysisRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.request.AnalyzeUrl
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.message.MessageAnalysis
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