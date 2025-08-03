package com.openlysis.data.analysis.di

import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.repository.AnalysesRepository
import com.openlysis.data.analysis.repository.DefaultAnalysesRepository
import com.openlysis.data.analysis.request.AnalyzeFile
import com.openlysis.data.analysis.request.AnalyzeUrl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for binding analysis-related repositories.
 *
 * This module provides singleton bindings for different types of [AnalysesRepository] implementations, each handling a specific analysis request and result type.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalysisBindingModule {
    @Singleton
    @Binds
    abstract fun bindUrlMultiAnalysesRepository(
        impl: DefaultAnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>
    ): AnalysesRepository<AnalyzeUrl, UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindFileMultiAnalysesRepository(
        impl: DefaultAnalysesRepository<AnalyzeFile, FileMultiAnalysis>
    ): AnalysesRepository<AnalyzeFile, FileMultiAnalysis>
}