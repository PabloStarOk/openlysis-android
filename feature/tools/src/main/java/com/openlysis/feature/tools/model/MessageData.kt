package com.openlysis.feature.tools.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a message.
 *
 * @property sender The sender of the message.
 * @property subject The subject of the message, nullable.
 * @property content The content of the message.
 */
@Parcelize
@Stable
internal data class MessageData(
    val sender: String = "",
    val subject: String? = null,
    val content: String = ""
) : Parcelable {
    val requiredFieldsSatisfied: Boolean
        get() = sender.isNotBlank() && content.isNotBlank()
}