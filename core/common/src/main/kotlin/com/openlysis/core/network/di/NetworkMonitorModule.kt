package com.openlysis.core.network.di

import com.openlysis.core.network.ConnectivityManagerNetworkMonitor
import com.openlysis.core.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing a singleton instance of [NetworkMonitor].
 * Binds [ConnectivityManagerNetworkMonitor] as the implementation for [NetworkMonitor].
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkMonitorModule {
    /**
     * Binds [ConnectivityManagerNetworkMonitor] to [NetworkMonitor] as a singleton.
     */
    @Singleton
    @Binds
    abstract fun bindNetworkMonitor(impl: ConnectivityManagerNetworkMonitor): NetworkMonitor
}