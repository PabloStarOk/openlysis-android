package com.openlysis.models.analysis

/**
 * Metadata about a file.
 *
 * @property name The name of the file.
 * @property contentType The MIME type of the file, indicating its format.
 * @property size The size of the file in bytes.
 */
data class FileMetadata(
    val name: String,
    val contentType: String,
    val size: Long
)