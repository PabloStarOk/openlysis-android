package com.openlysis.data.remote.signalr

import kotlin.reflect.KClass

/**
 * Provides methods to connect to SignalR Hubs.
 */
internal interface SignalRConnectionProvider {
    /**
     * Establishes a connection to a SignalR hub method and sets up a handler for incoming DTOs.
     *
     * @param hubMethod The SignalR hub method to connect to.
     * @param handler A function to handle incoming DTOs of type [TDto].
     * @param dtoClass The KClass of the DTO type.
     */
    suspend fun <TDto : Any> connect(
        hubMethod: SignalRHubMethod,
        handler: (TDto) -> Unit,
        dtoClass: KClass<TDto>
    )

    /**
     * Disconnects from the specified SignalR hub method.
     *
     * @param hubMethod The SignalR hub method to disconnect from.
     */
    suspend fun disconnect(hubMethod: SignalRHubMethod)
}