package com.openlysis.core.network.di

import com.openlysis.core.network.AppDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides coroutine scopes for application-wide usage.
 * Installed in the SingletonComponent to ensure singleton scope.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineScopesModule {
    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope(
        @Dispatcher(AppDispatcher.Default) dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}