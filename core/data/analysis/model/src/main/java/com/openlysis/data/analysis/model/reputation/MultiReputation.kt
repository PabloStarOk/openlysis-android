package com.openlysis.data.analysis.model.reputation

import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Verdict
import java.time.Instant

/**
 * The result of a multi-reputation, which is an aggregation of multiple individual reputations
 * evaluations performed on data.
 *
 * @param TReputation The specific type of reputation data being held. This type must be a subclass of [Reputation].
 * @property id A unique identifier for this multi-reputation analysis.
 * @property date The timestamp indicating when this multi-reputation analysis was performed.
 * @property finalVerdict The conclusive [Verdict] derived from the combined reputation sources.
 * @property data The data related to the multi-reputation, (e.g. An email address or phone number).
 * @property reputations A list of objects derived from [Reputation].
 */
class MultiReputation<TReputation>(
    id: String,
    val date: Instant,
    val finalVerdict: Verdict,
    val data: String,
    val reputations: List<TReputation>
) : Model(id)
    where TReputation : Reputation