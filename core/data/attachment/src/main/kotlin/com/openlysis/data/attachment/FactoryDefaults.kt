package com.openlysis.data.attachment

/**
 * Default values for file attachments.
 * Contains constants used as fallback values for attachment properties.
 */
internal object FactoryDefaults {
    /** Default MIME type used when the actual type cannot be determined */
    const val DEFAULT_MIME_TYPE = "application/octet-stream"

    /** Indicates an unknown or undefined file size */
    const val UNKNOWN_FILE_SIZE: Long = -1
}