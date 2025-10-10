package com.openlysis.data.remote.constant

/**
 * Constants for name of fields required on the analysis endpoints of the API.
 */
internal object ApiFields {
    // Common
    const val REANALYZE = "reanalyze"
    const val ANALYSIS_ID = "id"

    // Analyze URL
    const val URL = "url"

    // Analyze file
    const val FILE = "file"
    const val FILE_PASSWORD = "password"

    // Analyze message
    const val MESSAGE_TYPE = "messageType"
    const val MESSAGE_SENDER = "sender"
    const val MESSAGE_CONTENT = "content"
    const val MESSAGE_SUBJECT = "subject"
    const val MESSAGE_FILES = "attachedFiles"
    const val MESSAGE_PASSWORDS = "attachedFilesPasswords"
    const val MESSAGE_COUNTRY_CODE = "countryCode"

    // Get analyses
    const val ANALYSIS_TYPE = "type"
    const val PAGE = "page"
    const val PAGE_SIZE = "pageSize"
}