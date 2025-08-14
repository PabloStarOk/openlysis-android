package com.openlysis.data.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import javax.inject.Singleton

/**
 * Dagger module that provides authentication-related dependencies.
 * Installed in the SingletonComponent for application-wide scope.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AuthProvidingModule {
    @Singleton
    @Provides
    fun provideClock(): Clock = Clock.System
}