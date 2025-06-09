package com.openlysis.models.reputation

import com.openlysis.models.common.Verdict
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
data class MultiReputation<TReputation>(
    val id: String,
    val date: Instant,
    val finalVerdict: Verdict,
    val data: String,
    val reputations: List<TReputation>
) where TReputation : Reputation