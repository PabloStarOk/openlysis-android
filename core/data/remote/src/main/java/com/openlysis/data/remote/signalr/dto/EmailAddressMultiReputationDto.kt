package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import kotlinx.datetime.Instant

/**
 * DTO sent over SignalR for [MultiReputation] model (email address).
 *
 * @property id Unique identifier for the reputation entry.
 * @property evaluationDateMillis Evaluation date in milliseconds since epoch.
 * @property finalVerdict Final verdict for the email address reputation.
 * @property emailAddress The email address being evaluated.
 * @property reputations List of individual reputation DTOs for the email address.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class EmailAddressMultiReputationDto
    @JsonCreator
    constructor(
        @JsonProperty("Id")
        val id: String,
        @JsonProperty("EvaluationDate")
        val evaluationDateMillis: Long,
        @JsonProperty("FinalVerdict")
        val finalVerdict: Verdict,
        @JsonProperty("EmailAddress")
        val emailAddress: String,
        @JsonProperty("Reputations")
        val reputations: List<EmailAddressReputationDto>
    ) {
        /**
         * Converts this DTO to the domain model [MultiReputation].
         *
         * @return [MultiReputation] domain model instance.
         */
        internal fun convertToModel(): MultiReputation<EmailAddressReputation> =
            MultiReputation(
                id = id,
                evaluationDate = Instant.fromEpochMilliseconds(evaluationDateMillis),
                finalVerdict = finalVerdict,
                data = emailAddress,
                reputations = reputations.map { it.convertToModel() }
            )
    }