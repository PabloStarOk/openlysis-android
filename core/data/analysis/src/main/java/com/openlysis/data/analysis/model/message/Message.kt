package com.openlysis.data.analysis.model.message

/**
 * A message with various attributes.
 *
 * @property type The [MessageType] of the message.
 * @property sender The sender of the message.
 * @property content The content of the message.
 * @property subject The subject of the message (optional, can be null).
 */
data class Message(
    val type: MessageType,
    val sender: String,
    val content: String,
    val subject: String?
)