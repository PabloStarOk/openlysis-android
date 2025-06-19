package com.openlysis.data.database.di

import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.openlysis.data.database.source.EmailMultiReputationDataSource
import com.openlysis.data.database.source.FileMultiAnalysisDataSource
import com.openlysis.data.database.source.MessageAnalysisDataSource
import com.openlysis.data.database.source.PhoneMultiReputationDataSource
import com.openlysis.data.database.source.RelationalLocalDataSource
import com.openlysis.data.database.source.UrlMultiAnalysisDataSource
import com.openlysis.data.local.LocalRepository
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