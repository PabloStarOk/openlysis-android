package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation

/**
 * DTO sent over SignalR for [EmailAddressReputation] model.
 *
 * @property id Unique identifier for the reputation entry.
 * @property serviceName Name of the service that provided the reputation data.
 * @property verdict The verdict associated with the email address.
 * @property isDisposable Indicates if the email address is disposable.
 * @property isRiskyTld Indicates if the email address uses a risky top-level domain.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class EmailAddressReputationDto
    @JsonCreator
    constructor(
        @JsonProperty("Id")
        val id: String,
        @JsonProperty("ServiceName")
        val serviceName: String,
        @JsonProperty("Verdict")
        val verdict: Verdict,
        @JsonProperty("IsDisposable")
        val isDisposable: Boolean?,
        @JsonProperty("IsRiskyTld")
        val isRiskyTld: Boolean?
    ) {
        /**
         * Converts this DTO to the domain model [EmailAddressReputation].
         *
         * @return [EmailAddressReputation] domain model instance.
         */
        internal fun convertToModel(): EmailAddressReputation =
            EmailAddressReputation(
                id = id,
                serviceName = serviceName,
                verdict = verdict,
                isDisposable = isDisposable,
                isRiskyTld = isRiskyTld
            )
    }