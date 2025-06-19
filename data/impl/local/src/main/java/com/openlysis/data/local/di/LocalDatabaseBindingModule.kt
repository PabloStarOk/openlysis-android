package com.openlysis.data.local.di

import com.openlysis.data.local.LocalRepository
import com.openlysis.data.local.source.EmailMultiReputationDataSource
import com.openlysis.data.local.source.FileMultiAnalysisDataSource
import com.openlysis.data.local.source.MessageAnalysisDataSource
import com.openlysis.data.local.source.PhoneMultiReputationDataSource
import com.openlysis.data.local.source.RelationalLocalDataSource
import com.openlysis.data.local.source.UrlMultiAnalysisDataSource
import com.openlysis.models.analysis.FileMultiAnalysis
import com.openlysis.models.analysis.UrlMultiAnalysis
import com.openlysis.models.message.MessageAnalysis
import com.openlysis.models.reputation.EmailAddressReputation
import com.openlysis.models.reputation.MultiReputation
import com.openlysis.models.reputation.PhoneNumberReputation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that binds concrete local data source implementations to their
 * corresponding repository and relational data source interfaces for dependency injection.
 *
 * This module is installed in the [SingletonComponent] and enables injection of
 * [LocalRepository] and [RelationalLocalDataSource] for each supported entity type.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDatabaseBindingModule {
    @Singleton
    @Binds
    abstract fun bindLocalUrlMultiAnalysisRepo(
        impl: UrlMultiAnalysisDataSource
    ): LocalRepository<UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindLocalFileMultiAnalysisRepo(
        impl: FileMultiAnalysisDataSource
    ): LocalRepository<FileMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindLocalMessageAnalysisRepo(
        impl: MessageAnalysisDataSource
    ): LocalRepository<MessageAnalysis>

    @Singleton
    @Binds
    abstract fun bindLocalEmailMultiReputationRepo(
        impl: EmailMultiReputationDataSource
    ): LocalRepository<MultiReputation<EmailAddressReputation>>

    @Singleton
    @Binds
    abstract fun bindLocalPhoneMultiReputationRepo(
        impl: PhoneMultiReputationDataSource
    ): LocalRepository<MultiReputation<PhoneNumberReputation>>

    @Singleton
    @Binds
    abstract fun bindRelationalUrlMultiAnalysisRepo(
        impl: UrlMultiAnalysisDataSource
    ): RelationalLocalDataSource<UrlMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindRelationalFileMultiAnalysisRepo(
        impl: FileMultiAnalysisDataSource
    ): RelationalLocalDataSource<FileMultiAnalysis>

    @Singleton
    @Binds
    abstract fun bindRelationalMultiReputationRepo(
        impl: EmailMultiReputationDataSource
    ): RelationalLocalDataSource<MultiReputation<EmailAddressReputation>>

    @Singleton
    @Binds
    abstract fun bindRelationalPhoneMultiReputationRepo(
        impl: PhoneMultiReputationDataSource
    ): RelationalLocalDataSource<MultiReputation<PhoneNumberReputation>>
}