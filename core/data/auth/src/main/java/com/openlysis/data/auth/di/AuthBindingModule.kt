package com.openlysis.data.auth.di

import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.auth.DefaultAuthTokensManager
import com.openlysis.data.auth.DefaultTokenExpirationWatcher
import com.openlysis.data.auth.TokenExpirationWatcher
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
    abstract fun bindUserAuthDataRepository(impl: DefaultAuthTokensManager): AuthTokensManager

    @Singleton
    @Binds
    abstract fun bindTokenExpirationWatcher(
        impl: DefaultTokenExpirationWatcher
    ): TokenExpirationWatcher
}