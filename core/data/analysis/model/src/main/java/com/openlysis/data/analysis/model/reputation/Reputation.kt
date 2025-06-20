package com.openlysis.data.analysis.model.reputation

import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Verdict

/**
 * Represents the reputation of data.
 *
 * @property id The unique identifier of the reputation.
 * @property serviceName The name of the service that provided the reputation information.
 * @property verdict The overall [Verdict] or classification of the data.
 */
open class Reputation(
    id: String,
    val serviceName: String,
    val verdict: Verdict
) : Model(id)