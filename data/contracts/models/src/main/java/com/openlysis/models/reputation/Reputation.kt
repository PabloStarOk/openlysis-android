package com.openlysis.models.reputation

import com.openlysis.models.common.Verdict

/**
 * Represents the reputation of data.
 *
 * @property serviceName The name of the service that provided the reputation information.
 * @property verdict The overall [Verdict] or classification of the data.
 */
open class Reputation(
    val serviceName: String,
    val verdict: Verdict
)