package com.openlysis.data.remote

import java.net.URL

/**
 * Settings to configure the client of the API.
 *
 * @property baseUrl The base URL of the API.
 */
data class ApiClientSettings(
    val baseUrl: URL
)