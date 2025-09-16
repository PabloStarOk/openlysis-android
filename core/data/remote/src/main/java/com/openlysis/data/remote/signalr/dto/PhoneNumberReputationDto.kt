package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation

/**
 * DTO sent over SignalR for [PhoneNumberReputation] model.
 *
 * @property id Unique identifier for the reputation entry.
 * @property serviceName Name of the service that provided the reputation data.
 * @property verdict The verdict associated with the phone number.
 * @property phoneInfo Information about the phone number.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class PhoneNumberReputationDto
    @JsonCreator
    constructor(
        @JsonProperty("Id")
        val id: String,
        @JsonProperty("ServiceName")
        val serviceName: String,
        @JsonProperty("Verdict")
        val verdict: Verdict,
        @JsonProperty("PhoneInfo")
        val phoneInfo: PhoneInfoDto
    ) {
        /**
         * Converts this DTO to the domain model [PhoneNumberReputation].
         *
         * @return [PhoneNumberReputation] domain model instance.
         */
        internal fun convertToModel(): PhoneNumberReputation =
            PhoneNumberReputation(
                id = id,
                serviceName = serviceName,
                verdict = verdict,
                localFormat = phoneInfo.localFormat,
                countryCode = phoneInfo.countryCode,
                dialingCode = phoneInfo.dialingCode,
                lineType = phoneInfo.lineType
            )
    }