package com.openlysis.data.analysis.model.reputation

import com.openlysis.data.analysis.model.common.Verdict

/**
 * The reputation details of a phone number as determined or calculated from a specific service.
 *
 * @property serviceName The name of the service that provided the reputation information.
 * @property verdict The overall [Verdict] or classification of the data.
 * @property localFormat The phone number in its local format.
 * @property countryCode The ISO 3166-1 alpha-2 country code associated with the phone number.
 * @property dialingCode The international dialing code for the country.
 * @property lineType The type of phone line.
 */
class PhoneNumberReputation(
    id: String,
    serviceName: String,
    verdict: Verdict,
    val localFormat: String,
    val countryCode: String,
    val dialingCode: Int,
    val lineType: String
) : Reputation(id, serviceName, verdict)