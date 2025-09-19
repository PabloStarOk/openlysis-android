package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * DTO sent over SignalR for phone info model.
 *
 * @property localFormat The phone number in local format.
 * @property countryCode The ISO country code.
 * @property dialingCode The international dialing code.
 * @property lineType The type of phone line (e.g., mobile, landline).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class PhoneInfoDto
    @JsonCreator
    constructor(
        @JsonProperty("LocalFormat")
        val localFormat: String,
        @JsonProperty("CountryCode")
        val countryCode: String,
        @JsonProperty("DialingCode")
        val dialingCode: Int,
        @JsonProperty("LineType")
        val lineType: String
    )