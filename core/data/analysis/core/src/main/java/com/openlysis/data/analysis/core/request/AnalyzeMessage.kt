package com.openlysis.data.analysis.core.request

/**
 * A request to analyze a message.
 *
 * @property message [Message] containing the data of the message.
 * @property reanalyze Whether the message must be analyzed or it should try to fetch an existing analysis first
 * @property countryCode A code of a country associated to this message in ISO 3166-1 Alpha-2 format. This value is used to improve identification of phone numbers extracted from the message.
 */
data class AnalyzeMessage(
    val message: Message,
    val reanalyze: Boolean,
    val countryCode: String
)