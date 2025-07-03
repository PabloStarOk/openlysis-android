package com.openlysis.feature.tools.components

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

/**
 * State holder for [MessageSection].
 *
 * @property sender The sender of the message.
 * @property subject The subject of the message, nullable.
 * @property content The content of the message.
 */
@Parcelize
@Stable
internal data class MessageState(
    val sender: String = "",
    val subject: String? = null,
    val content: String = ""
) : Parcelable {
    val submitEnabled: Boolean
        get() = sender.isNotBlank() && content.isNotBlank()
}