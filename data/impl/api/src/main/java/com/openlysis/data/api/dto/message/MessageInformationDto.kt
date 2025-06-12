package com.openlysis.data.api.dto.message

import com.openlysis.models.common.HashValues
import com.openlysis.models.message.Message
import com.openlysis.models.message.MessageType
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object (DTO) for the [Message] model.
 *
 * @property type The type of the message as a string.
 * @property sender The sender of the message.
 * @property content The content of the message.
 * @property subject The subject of the message, nullable.
 * @property messageHashValues Hash values associated with the message.
 */
@JsonClass(generateAdapter = true)
internal data class MessageInformationDto(
    val type: String,
    val sender: String,
    val content: String,
    val subject: String?,
    val messageHashValues: HashValues
) {
    /**
     * Converts this DTO to the domain model [Message].
     *
     * @return [Message] domain model instance.
     */
    internal fun convertToModel(): Message =
        Message(
            type = MessageType.parse(type),
            sender = sender,
            content = content,
            subject = subject
        )
}