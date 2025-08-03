package com.openlysis.data.analysis.model.analysis

import com.squareup.moshi.JsonClass

/**
 * Metadata about a file.
 *
 * @property name The name of the file.
 * @property contentType The MIME type of the file, indicating its format.
 * @property size The size of the file in bytes.
 */
@JsonClass(generateAdapter = true)
data class FileMetadata(
    val name: String,
    val contentType: String,
    val size: Long
)