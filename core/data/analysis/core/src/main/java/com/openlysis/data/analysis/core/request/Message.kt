package com.openlysis.data.analysis.core.request

import com.openlysis.data.analysis.model.message.MessageType

/**
 * A message with various attributes and optional attachments.
 *
 * @property type A [MessageType].
 * @property sender The sender of the message.
 * @property content The content of the message.
 * @property subject The subject of the message (optional, can be null).
 * @property attachments A list of [Attachment] objects associated with the message.
 */
data class Message(
    val type: MessageType,
    val sender: String,
    val content: String,
    val subject: String?,
    val attachments: List<Attachment>?
)