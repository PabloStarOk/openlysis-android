package com.openlysis.data.auth.di

import com.openlysis.data.auth.DefaultUserAuthDataRepository
import com.openlysis.data.auth.UserAuthDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for binding authentication-related dependencies.
 * Installed in the SingletonComponent to provide application-wide singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthBindingModule {
    @Singleton
    @Binds
    abstract fun bindUserAuthDataRepository(
        impl: DefaultUserAuthDataRepository
    ): UserAuthDataRepository
}