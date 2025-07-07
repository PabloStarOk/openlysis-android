package com.openlysis.data.remote.di

import com.openlysis.data.analysis.core.repository.AnalysesRepository
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.request.AnalyzeUrl
import com.openlysis.data.analysis.core.source.AnalysesRemoteDataSource
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.remote.source.FileMultiAnalysesRemoteDataSource
import com.openlysis.data.remote.source.UrlMultiAnalysesRemoteDataSource
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
 * [AnalysesRepository] for each supported analysis type, backed by remote API calls.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class ApiBindingModule {
    @Singleton
    @Binds
    abstract fun bindUrlMultiAnalysisRepo(
        impl: UrlMultiAnalysesRemoteDataSource
    ): AnalysesRemoteDataSource<AnalyzeUrl, UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindFileMultiAnalysisRepo(
        impl: FileMultiAnalysesRemoteDataSource
    ): AnalysesRemoteDataSource<AnalyzeFile, FileMultiAnalysis>
}