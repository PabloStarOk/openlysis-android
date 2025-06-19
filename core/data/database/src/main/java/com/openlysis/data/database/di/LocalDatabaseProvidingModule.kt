package com.openlysis.data.database.di

import android.content.Context
import androidx.room.Room
import com.openlysis.data.database.AppDatabase
import com.openlysis.data.database.LocalStoragePreferences
import com.openlysis.data.database.constant.AppDatabaseInfo
import com.openlysis.data.database.dao.EmailMultiReputationDao
import com.openlysis.data.database.dao.EmailReputationDao
import com.openlysis.data.database.dao.FileAnalysisDao
import com.openlysis.data.database.dao.FileMultiAnalysisDao
import com.openlysis.data.database.dao.MessageAnalysisDao
import com.openlysis.data.database.dao.PhoneMultiReputationDao
import com.openlysis.data.database.dao.PhoneReputationDao
import com.openlysis.data.database.dao.UrlAnalysisDao
import com.openlysis.data.database.dao.UrlMultiAnalysisDao
import com.openlysis.data.database.source.LocalDataSourceState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides Room database and DAO instances, as well as
 * [LocalDataSourceState] singletons for each local data source type.
 *
 * This module is installed in the [SingletonComponent] and is responsible for
 * creating and providing all Room-related dependencies and entity state trackers
 * for the local database layer.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object LocalDatabaseProvidingModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = AppDatabaseInfo.NAME
            ).build()

    @Singleton
    @Provides
    fun provideUrlAnalysisDao(db: AppDatabase): UrlAnalysisDao = db.urlAnalysisDao()

    @Singleton
    @Provides
    fun provideUrlMultiAnalysisDao(db: AppDatabase): UrlMultiAnalysisDao = db.urlMultiAnalysisDao()

    @Singleton
    @Provides
    fun provideFileAnalysisDao(db: AppDatabase): FileAnalysisDao = db.fileAnalysisDao()

    @Singleton
    @Provides
    fun provideFileMultiAnalysisDao(db: AppDatabase): FileMultiAnalysisDao =
        db.fileMultiAnalysisDao()

    @Singleton
    @Provides
    fun provideMessageAnalysisDao(db: AppDatabase): MessageAnalysisDao = db.messageAnalysisDao()

    @Singleton
    @Provides
    fun provideEmailReputationDao(db: AppDatabase): EmailReputationDao = db.emailReputationDao()

    @Singleton
    @Provides
    fun provideEmailMultiReputationDao(db: AppDatabase): EmailMultiReputationDao =
        db.emailMultiReputationDao()

    @Singleton
    @Provides
    fun providePhoneReputationDao(db: AppDatabase): PhoneReputationDao = db.phoneReputationDao()

    @Singleton
    @Provides
    fun providePhoneMultiReputationDao(db: AppDatabase): PhoneMultiReputationDao =
        db.phoneMultiReputationDao()

    @UrlMultiAnalysisDsState
    @Singleton
    @Provides
    fun provideUrlMultiAnalysisState(
        settings: LocalStoragePreferences,
        dao: UrlMultiAnalysisDao
    ): LocalDataSourceState =
        runBlocking {
            LocalDataSourceState(
                maxStoredEntities = settings.maxStoredUrlMultiAnalyses,
                currentStoredEntities = dao.countWithoutParent()
            )
        }

    @FileMultiAnalysisDsState
    @Singleton
    @Provides
    fun provideFileMultiAnalysisState(
        settings: LocalStoragePreferences,
        dao: FileMultiAnalysisDao
    ): LocalDataSourceState =
        runBlocking {
            LocalDataSourceState(
                maxStoredEntities = settings.maxStoredFileMultiAnalyses,
                currentStoredEntities = dao.countWithoutParent()
            )
        }

    @MessageAnalysisDsState
    @Singleton
    @Provides
    fun provideMessageAnalysisState(
        settings: LocalStoragePreferences,
        dao: MessageAnalysisDao
    ): LocalDataSourceState =
        runBlocking {
            LocalDataSourceState(
                maxStoredEntities = settings.maxStoredMessageAnalyses,
                currentStoredEntities = dao.count()
            )
        }

    @EmailMultiReputationDsState
    @Singleton
    @Provides
    fun provideEmailMultiReputationState(
        settings: LocalStoragePreferences,
        dao: EmailMultiReputationDao
    ): LocalDataSourceState =
        runBlocking {
            LocalDataSourceState(
                maxStoredEntities = settings.maxStoredEmailMultiReputations,
                currentStoredEntities = dao.countWithoutParent()
            )
        }

    @PhoneMultiReputationDsState
    @Singleton
    @Provides
    fun providePhoneMultiReputationState(
        settings: LocalStoragePreferences,
        dao: PhoneMultiReputationDao
    ): LocalDataSourceState =
        runBlocking {
            LocalDataSourceState(
                maxStoredEntities = settings.maxStoredPhoneMultiReputations,
                currentStoredEntities = dao.countWithoutParent()
            )
        }
}