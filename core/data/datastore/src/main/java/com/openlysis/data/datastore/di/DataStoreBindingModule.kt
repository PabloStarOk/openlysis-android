package com.openlysis.data.datastore.di

import com.openlysis.data.auth.UserAuthLocalDataSource
import com.openlysis.data.datastore.EncryptedUserAuthLocalDataSource
import com.openlysis.data.datastore.UserPreferencesLocalDataSource
import com.openlysis.data.user.UserDataLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for binding DataStore-related dependencies.
 * Installed in the SingletonComponent to provide application-wide singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStoreBindingModule {
    @Singleton
    @Binds
    abstract fun bindUserAuthLocalDataSource(
        impl: EncryptedUserAuthLocalDataSource
    ): UserAuthLocalDataSource

    @Singleton
    @Binds
    abstract fun bindUserDataLocalDataSource(
        impl: UserPreferencesLocalDataSource
    ): UserDataLocalDataSource
}