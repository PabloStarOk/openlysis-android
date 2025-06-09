package com.openlysis.models.reputation

import com.openlysis.models.common.Verdict

/**
 * The reputation details of an email address as determined or calculated from a specific service.
 *
 * @property serviceName The name of the service that provided the reputation information.
 * @property verdict The overall [Verdict] or classification of the data.
 * @property isDisposable A boolean flag indicating whether the email address is associated with a disposable or temporary email service. `true` if it is, `false` otherwise.
 * @property isRiskyTld A boolean flag indicating whether the email address's top-level domain (TLD) is considered risky or commonly associated with spam or malicious activities. `true` if it is, `false` otherwise.
 */
class EmailAddressReputation(
    serviceName: String,
    verdict: Verdict,
    val isDisposable: Boolean,
    val isRiskyTld: Boolean
) : Reputation(serviceName, verdict)