package com.openlysis.notification.di

import com.openlysis.notification.Notifier
import com.openlysis.notification.SystemTrayNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing notification-related dependencies.
 * Binds [SystemTrayNotifier] as the implementation for [Notifier].
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationModule {
    /**
     * Binds [SystemTrayNotifier] to [Notifier] as a singleton.
     *
     * @param impl The implementation of [Notifier].
     * @return The bound [Notifier] instance.
     */
    @Singleton
    @Binds
    abstract fun bindNotifier(impl: SystemTrayNotifier): Notifier
}