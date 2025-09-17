package com.openlysis.data.remote.signalr

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionState
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.core.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okio.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * Default implementation of [SignalRConnectionProvider] for managing SignalR hub connections.
 *
 * @param stopDelayMillis Delay in milliseconds before stopping the hub connection after all handlers are removed.
 * @param hubConnection The SignalR [HubConnection] instance to manage.
 * @param appScope The application-wide [CoroutineScope] for launching coroutines.
 * @param ioDispatcher The [CoroutineDispatcher] used for IO operations.
 */
internal class DefaultSignalRConnectionProvider(
    private val stopDelayMillis: Long,
    private val hubConnection: HubConnection,
    @ApplicationScope private val appScope: CoroutineScope,
    @Dispatcher(AppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : SignalRConnectionProvider {
    private val connectionMutex = Mutex()
    private val activeConnections = ConcurrentHashMap.newKeySet<SignalRHubMethod>()
    private var stopConnectionJob: Job? = null
    private var closedIntentionally: Boolean = false

    private val _isAvailable = MutableStateFlow<Boolean>(true)
    override val isAvailable: StateFlow<Boolean> = _isAvailable.asStateFlow()

    /**
     * Start listening for SignalR hub connection events.
     */
    fun listenForConnectionEvents() {
        hubConnection.onClosed { exception ->
            if (exception == null && closedIntentionally) return@onClosed
            _isAvailable.value = false
        }
    }

    override suspend fun <TDto : Any> connect(
        hubMethod: SignalRHubMethod,
        handler: (TDto) -> Unit,
        dtoClass: KClass<TDto>
    ) {
        if (activeConnections.contains(hubMethod)) return

        hubConnection.on(hubMethod.name, handler, dtoClass.java)
        activeConnections.add(hubMethod)
        connectionMutex.withLock {
            stopConnectionJob?.cancel()
            stopConnectionJob = null
            ensureHubConnected()
        }
    }

    override suspend fun disconnect(hubMethod: SignalRHubMethod) {
        if (!activeConnections.contains(hubMethod)) return

        hubConnection.remove(hubMethod.name)
        activeConnections.remove(hubMethod)
        connectionMutex.withLock {
            if (activeConnections.isEmpty() && stopConnectionJob == null) {
                stopConnectionJob = startStopJob()
            }
        }
    }

    private suspend fun ensureHubConnected() =
        withContext(ioDispatcher) {
            if (hubConnection.connectionState != HubConnectionState.DISCONNECTED) return@withContext

            closedIntentionally = false

            try {
                hubConnection.start().await()
                _isAvailable.value = true
            } catch (_: IOException) {
                _isAvailable.value = false
            }
        }

    private fun startStopJob(): Job =
        appScope.launch(ioDispatcher) {
            delay(stopDelayMillis)
            stop()
        }

    private suspend fun stop() {
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) return
        if (activeConnections.isNotEmpty()) return
        closedIntentionally = true
        hubConnection.stop().await()
    }
}