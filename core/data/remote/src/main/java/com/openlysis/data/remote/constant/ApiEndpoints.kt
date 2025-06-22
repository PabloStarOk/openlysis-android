package com.openlysis.data.remote.constant

/**
 * Constants for each endpoint address of the API.
 */
internal object ApiEndpoints {
    const val ANALYZE_URL = "urls"
    const val ANALYZE_FILE = "files"
    const val ANALYZE_MESSAGE = "messages"

    const val GET_URL_MULTI_ANALYSIS = "urls/analyses/{id}"
    const val GET_FILE_MULTI_ANALYSIS = "files/analyses/{id}"
    const val GET_MESSAGE_ANALYSIS = "messages/analyses/{id}"
    const val GET_ANALYSES = "users/analyses"
}