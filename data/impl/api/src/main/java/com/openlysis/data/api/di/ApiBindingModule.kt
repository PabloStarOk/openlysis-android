package com.openlysis.data.api.di

import com.openlysis.data.api.repository.FileMultiAnalysisRepository
import com.openlysis.data.api.repository.MessageAnalysisRepository
import com.openlysis.data.api.repository.UrlMultiAnalysisRepository
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
import javax.inject.Singleton

/**
 * Dagger Hilt module that binds API-based repository implementations to their
 * corresponding remote repository interfaces for dependency injection.
 *
 * This module is installed in the [SingletonComponent] and enables injection of
 * [AnalysisRepository] for each supported analysis type, backed by remote API calls.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class ApiBindingModule {
    @Singleton
    @Binds
    abstract fun bindUrlMultiAnalysisRepo(
        impl: UrlMultiAnalysisRepository
    ): AnalysisRepository<AnalyzeUrl, UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindFileMultiAnalysisRepo(
        impl: FileMultiAnalysisRepository
    ): AnalysisRepository<AnalyzeFile, FileMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindMessageAnalysisRepo(
        impl: MessageAnalysisRepository
    ): AnalysisRepository<AnalyzeMessage, MessageAnalysis>
}