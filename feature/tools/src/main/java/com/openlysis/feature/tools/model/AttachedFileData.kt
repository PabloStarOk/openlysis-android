package com.openlysis.feature.tools.model

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

/**
 * Data class representing an attached file.
 *
 * @property id An random integer used to identify the attached file in the app.
 * @property uri An Android URI of the file.
 * @property displayName The display name of the file.
 * @property size The size of the file in bytes.
 * @property password Optional password for the file, defaults to an empty string.
 * @property error Optional error associated with the file, defaults to null.
 */
@Parcelize
@Stable
@Immutable
internal data class AttachedFileData(
    val id: Int = Random.nextInt(),
    val uri: Uri,
    val displayName: String,
    val size: Long,
    val password: String = "",
    val error: AttachedFileError? = null
) : Parcelable