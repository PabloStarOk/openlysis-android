package com.openlysis.models.message

/**
 * Type of a message.
 *
 * @property Sms Represents a Short Message Service (SMS) message.
 * @property Email Represents an electronic mail (email) message.
 */
enum class MessageType {
    Sms,
    Email
}