package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import kotlinx.datetime.Instant

/**
 * DTO sent over SignalR for [MultiReputation] model (phone number).
 *
 * @property id Unique identifier for the reputation entry.
 * @property evaluationDateMillis Evaluation date in milliseconds since epoch.
 * @property finalVerdict Final verdict for the phone number reputation.
 * @property phoneNumber The phone number being evaluated.
 * @property reputations List of individual reputation DTOs for the phone number.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class PhoneNumberMultiReputationDto
    @JsonCreator
    constructor(
        @JsonProperty("Id")
        val id: String,
        @JsonProperty("EvaluationDate")
        val evaluationDateMillis: Long,
        @JsonProperty("FinalVerdict")
        val finalVerdict: Verdict,
        @JsonProperty("PhoneNumber")
        val phoneNumber: String,
        @JsonProperty("Reputations")
        val reputations: List<PhoneNumberReputationDto>
    ) {
        /**
         * Converts this DTO to the domain model [MultiReputation].
         *
         * @return [MultiReputation] domain model instance.
         */
        internal fun convertToModel(): MultiReputation<PhoneNumberReputation> =
            MultiReputation<PhoneNumberReputation>(
                id = id,
                evaluationDate = Instant.fromEpochMilliseconds(evaluationDateMillis),
                finalVerdict = finalVerdict,
                data = phoneNumber,
                reputations = reputations.map { r -> r.convertToModel() }
            )
    }