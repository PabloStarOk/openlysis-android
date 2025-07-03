package com.openlysis.feature.tools.data

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing an attached file.
 *
 * @property uri An Android URI of the file.
 * @property displayName The display name of the file.
 * @property size The size of the file in bytes.
 * @property password Optional password for the file, defaults to an empty string.
 */
@Parcelize
@Stable
@Immutable
internal data class AttachedFileData(
    val uri: Uri,
    val displayName: String,
    val size: Long,
    val password: String? = null
) : Parcelable