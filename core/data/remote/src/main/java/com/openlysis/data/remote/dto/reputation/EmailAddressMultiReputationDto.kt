package com.openlysis.data.remote.dto.reputation

import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.squareup.moshi.JsonClass
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant

/**
 * Data Transfer Object (DTO) for the [MultiReputation] model.
 *
 * @property id Unique identifier for the reputation entry.
 * @property evaluationDate ISO-8601 formatted date string of the evaluation.
 * @property finalVerdict String representation of the final verdict.
 * @property emailAddress The email address being evaluated.
 * @property reputations List of individual email address reputation DTOs.
 */
@JsonClass(generateAdapter = true)
internal data class EmailAddressMultiReputationDto(
    val id: String,
    val evaluationDate: String,
    val finalVerdict: String,
    val emailAddress: String,
    val reputations: List<EmailAddressReputationDto>
) {
    /**
     * Converts this DTO to the domain model [MultiReputation].
     *
     * @return [MultiReputation] domain model instance.
     */
    internal fun convertToModel(): MultiReputation<EmailAddressReputation> =
        MultiReputation<EmailAddressReputation>(
            id = id,
            evaluationDate = Instant.parse(evaluationDate).toJavaInstant(),
            finalVerdict = Verdict.parse(finalVerdict),
            data = emailAddress,
            reputations = reputations.map { r -> r.convertToModel() }
        )
}