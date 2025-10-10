package com.openlysis.data.analysis.model.message

import com.openlysis.data.analysis.model.message.MessageType.Email
import com.openlysis.data.analysis.model.message.MessageType.Sms

/**
 * Type of a message.
 *
 * @property Sms Represents a Short Message Service (SMS) message.
 * @property Email Represents an electronic mail (email) message.
 */
enum class MessageType {
    Sms,
    Email;

    companion object {
        /**
         * Parses the given string value to a [MessageType].
         *
         * @param value The string representation of the message type.
         * @param ignoreCase Whether to ignore case when matching the value. Defaults to true.
         * @return The corresponding [MessageType] if found.
         * @throws IllegalArgumentException if the value does not match any [MessageType].
         */
        fun parse(
            value: String,
            ignoreCase: Boolean = true
        ): MessageType =
            MessageType.entries.find {
                it.name.equals(value, ignoreCase)
            } ?: throw IllegalArgumentException("Invalid message type: $value")
    }
}