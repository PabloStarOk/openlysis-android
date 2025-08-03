package com.openlysis.data.database.di

import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.openlysis.data.analysis.source.AnalysesLocalDataSource
import com.openlysis.data.database.source.EmailMultiReputationsLocalDataSource
import com.openlysis.data.database.source.FileMultiAnalysesLocalDataSource
import com.openlysis.data.database.source.PhoneMultiReputationsLocalDataSource
import com.openlysis.data.database.source.RelationalLocalDataSource
import com.openlysis.data.database.source.UrlMultiAnalysesLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that binds concrete local data source implementations to their
 * corresponding data source interfaces for dependency injection.
 *
 * This module is installed in the [SingletonComponent] and enables injection of
 * [AnalysesLocalDataSource] and [RelationalLocalDataSource] for each supported entity type.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDatabaseBindingModule {
    @Singleton
    @Binds
    abstract fun bindLocalUrlMultiAnalysisRepo(
        impl: UrlMultiAnalysesLocalDataSource
    ): AnalysesLocalDataSource<UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindLocalFileMultiAnalysisRepo(
        impl: FileMultiAnalysesLocalDataSource
    ): AnalysesLocalDataSource<FileMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindLocalEmailMultiReputationRepo(
        impl: EmailMultiReputationsLocalDataSource
    ): AnalysesLocalDataSource<MultiReputation<EmailAddressReputation>>

    @Singleton
    @Binds
    abstract fun bindLocalPhoneMultiReputationRepo(
        impl: PhoneMultiReputationsLocalDataSource
    ): AnalysesLocalDataSource<MultiReputation<PhoneNumberReputation>>

    @Singleton
    @Binds
    abstract fun bindRelationalUrlMultiAnalysisRepo(
        impl: UrlMultiAnalysesLocalDataSource
    ): RelationalLocalDataSource<UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindRelationalFileMultiAnalysisRepo(
        impl: FileMultiAnalysesLocalDataSource
    ): RelationalLocalDataSource<FileMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindRelationalMultiReputationRepo(
        impl: EmailMultiReputationsLocalDataSource
    ): RelationalLocalDataSource<MultiReputation<EmailAddressReputation>>

    @Singleton
    @Binds
    abstract fun bindRelationalPhoneMultiReputationRepo(
        impl: PhoneMultiReputationsLocalDataSource
    ): RelationalLocalDataSource<MultiReputation<PhoneNumberReputation>>
}