package com.openlysis.data.api.dto.reputation

import com.openlysis.models.common.Verdict
import com.openlysis.models.reputation.EmailAddressReputation
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object (DTO) for the [EmailAddressReputation] model.
 *
 * @property serviceName The name of the service providing the reputation.
 * @property verdict The verdict as a string, to be parsed into [Verdict].
 * @property isDisposable Indicates if the email address is disposable.
 * @property isRiskyTld Indicates if the email address uses a risky TLD.
 */
@JsonClass(generateAdapter = true)
internal data class EmailAddressReputationDto(
    val id: String,
    val serviceName: String,
    val verdict: String,
    val isDisposable: Boolean?,
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
            verdict = Verdict.parse(verdict),
            isDisposable = isDisposable,
            isRiskyTld = isRiskyTld
        )
}