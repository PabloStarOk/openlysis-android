package com.openlysis.data.api.dto.reputation

import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object representing phone information.
 *
 * @property localFormat The phone number in local format.
 * @property countryCode The ISO country code.
 * @property dialingCode The international dialing code.
 * @property lineType The type of phone line (e.g., mobile, landline).
 */
@JsonClass(generateAdapter = true)
internal data class PhoneInfoDto(
    val localFormat: String,
    val countryCode: String,
    val dialingCode: Int,
    val lineType: String
)