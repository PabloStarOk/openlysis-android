package com.openlysis.data.remote.dto.reputation

import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object (DTO) for the [PhoneNumberReputation] model.
 *
 * @property serviceName Name of the service providing the reputation.
 * @property verdict String representation of the verdict.
 * @property phoneInfo DTO containing phone number information.
 */
@JsonClass(generateAdapter = true)
internal data class PhoneNumberReputationDto(
    val id: String,
    val serviceName: String,
    val verdict: String,
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
            verdict = Verdict.parse(verdict),
            localFormat = phoneInfo.localFormat,
            countryCode = phoneInfo.countryCode,
            dialingCode = phoneInfo.dialingCode,
            lineType = phoneInfo.lineType
        )
}