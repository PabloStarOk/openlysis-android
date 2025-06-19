package com.openlysis.data.remote.dto.reputation

import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.squareup.moshi.JsonClass
import java.time.Instant

/**
 * Data Transfer Object (DTO) for the [MultiReputation] model.
 *
 * @property id Unique identifier for the reputation entry.
 * @property evaluationDate ISO-8601 formatted date string of the evaluation.
 * @property finalVerdict String representation of the final verdict.
 * @property phoneNumber The phone number associated with the reputations.
 * @property reputations List of [PhoneNumberReputationDto] representing individual reputations.
 */
@JsonClass(generateAdapter = true)
internal data class PhoneNumberMultiReputationDto(
    val id: String,
    val evaluationDate: String,
    val finalVerdict: String,
    val phoneNumber: String,
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
            date = Instant.parse(evaluationDate),
            finalVerdict = Verdict.parse(finalVerdict),
            data = phoneNumber,
            reputations = reputations.map { r -> r.convertToModel() }
        )
}