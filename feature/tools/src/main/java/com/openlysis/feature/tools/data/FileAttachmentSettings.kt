package com.openlysis.feature.tools.data

import androidx.compose.runtime.Immutable

/**
 * Immutable settings for file attachments.
 *
 * @property maxFilesAmount Maximum number of files that can be attached.
 * @property maxFileSize Maximum allowed size for each file, unit is determined by []].
 * @property mimeTypeFilter MIME type filter for allowed file types. Defaults to any.
 */
@Immutable
data class FileAttachmentSettings(
    val maxFilesAmount: Int,
    val maxFileSize: Long,
    val mimeTypeFilter: String = "*/*"
)