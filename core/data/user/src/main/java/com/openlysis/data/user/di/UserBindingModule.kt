package com.openlysis.data.user.di

import com.openlysis.data.user.DefaultUserDataRepository
import com.openlysis.data.user.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for binding user-related dependencies.
 * Installs bindings in the SingletonComponent scope.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserBindingModule {
    @Singleton
    @Binds
    abstract fun bindUserDataRepository(impl: DefaultUserDataRepository): UserDataRepository
}