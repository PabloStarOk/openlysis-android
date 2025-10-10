package com.openlysis.core.network

import kotlinx.coroutines.flow.Flow

/**
 * Monitors the network connectivity status.
 */
interface NetworkMonitor {
    /**
     * A [Flow] that emits the current online status of the device.
     * Emits `true` if online, `false` otherwise.
     */
    val isOnline: Flow<Boolean>
}