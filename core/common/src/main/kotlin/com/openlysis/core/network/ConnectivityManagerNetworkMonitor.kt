package com.openlysis.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.openlysis.core.network.di.Dispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * NetworkMonitor implementation using Android's ConnectivityManager.
 *
 * @param context Application context used to access system services.
 * @param ioDispatcher Coroutine dispatcher for network monitoring operations.
 */
internal class ConnectivityManagerNetworkMonitor
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        @Dispatcher(AppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
    ) : NetworkMonitor {
        override val isOnline: Flow<Boolean> =
            callbackFlow {
                val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
                if (connectivityManager == null) {
                    channel.trySend(false)
                    channel.close()
                    return@callbackFlow
                }

                val callback = TrackNetworksCallback(channel)
                val request =
                    NetworkRequest
                        .Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build()
                connectivityManager.registerNetworkCallback(request, callback)

                channel.trySend(connectivityManager.isCurrentlyConnected())

                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }.flowOn(ioDispatcher).conflate()

        private class TrackNetworksCallback(
            val channel: SendChannel<Boolean>
        ) : ConnectivityManager.NetworkCallback() {
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                channel.trySend(networks.isNotEmpty())
            }
        }

        private fun ConnectivityManager.isCurrentlyConnected() =
            activeNetwork
                ?.let(::getNetworkCapabilities)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }