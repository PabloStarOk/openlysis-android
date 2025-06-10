package com.openlysis.data.remote.request

import java.io.File

/**
 * A file attached to a message.
 *
 * @property file The file related to the attachment.
 * @property password An optional password to unlock the file.
 */
data class Attachment(
    val file: File,
    val password: String?
)