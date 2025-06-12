package com.openlysis.data.remote.request

import java.io.File

/**
 * A single file to be analyzed.
 *
 * @property file The file instance.
 * @property mimeType The MIME type of the file.
 * @property password An optional password to unlock the file.
 */
data class Attachment(
    val file: File,
    val mimeType: String,
    val password: String?
)