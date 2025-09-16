package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.message.Message
import com.openlysis.data.analysis.model.message.MessageType

/**
 * DTO sent over SignalR for [Message] model.
 *
 * @property type The type of message (e.g., email, SMS).
 * @property sender The sender of the message.
 * @property content The content of the message.
 * @property subject The subject of the message (nullable).
 * @property hashValues Hash values for the message content.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class MessageInformationDto
    @JsonCreator
    constructor(
        @JsonProperty("Type")
        val type: MessageType,
        @JsonProperty("Sender")
        val sender: String,
        @JsonProperty("Content")
        val content: String,
        @JsonProperty("Subject")
        val subject: String?,
        @JsonProperty("HashValues")
        val hashValues: HashValuesDto
    ) {
        /**
         * Converts this DTO to the domain model [Message].
         *
         * @return [Message] domain model instance.
         */
        internal fun convertToModel(): Message =
            Message(
                type = type,
                sender = sender,
                content = content,
                subject = subject
            )
    }