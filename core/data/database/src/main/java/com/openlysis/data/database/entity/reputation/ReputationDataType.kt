package com.openlysis.data.database.entity.reputation

/**
 * Enum representing the type of data for a reputation check.
 *
 * - [EmailAddress]: The data is an email address.
 * - [PhoneNumber]: The data is a phone number.
 */
internal enum class ReputationDataType {
    EmailAddress,
    PhoneNumber
}