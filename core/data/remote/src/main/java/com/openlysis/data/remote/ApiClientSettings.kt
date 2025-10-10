package com.openlysis.data.remote

import java.net.URL

/**
 * Settings to configure the clients of the APIs.
 *
 * @property analysisApiBaseUrl The base URL of the analysis API.
 * @property authApiBaseUrl The base URL of the authentication API.
 * @property analysisUpdatesSignalRHubUrl The URL of the SignalR hub for analysis updates.
 */
data class ApiClientSettings(
    val analysisApiBaseUrl: URL,
    val authApiBaseUrl: URL,
    val analysisUpdatesSignalRHubUrl: URL
)