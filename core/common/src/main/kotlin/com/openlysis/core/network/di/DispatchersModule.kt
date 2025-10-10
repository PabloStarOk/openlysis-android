package com.openlysis.core.network.di

import com.openlysis.core.network.AppDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Dagger Hilt module that provides CoroutineDispatchers for dependency injection.
 * Installed in the SingletonComponent to ensure single instances across the app.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object DispatchersModule {
    @Dispatcher(AppDispatcher.Default)
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Dispatcher(AppDispatcher.IO)
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}