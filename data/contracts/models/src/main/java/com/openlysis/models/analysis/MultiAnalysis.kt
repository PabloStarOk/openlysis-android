package com.openlysis.models.analysis

import com.openlysis.models.common.Verdict
import java.time.Instant

/**
 * The result of a multi-analysis, which is an aggregation of multiple individual analyses
 * performed on data.
 *
 * @property id A unique identifier for this multi-analysis.
 * @property startedDate The exact moment when the multi-analysis process was initiated.
 * @property status The current overall [AnalysisStatus] of the multi-analysis.
 * @property finalVerdict The conclusive [Verdict] of the analyzed item based on the combined results of all individual analyses.
 * @property avgThreatScore An optional average threat score calculated from the threat scores of the individual analyses. This can be null if not applicable or if no individual analyses provided a score.
 * @property hashValues A [HashValues] object calculated for the analyzed artifact.
 * @property analyses A list of [Analysis] objects.
 */
open class MultiAnalysis(
    val id: String,
    val startedDate: Instant,
    val status: AnalysisStatus,
    val finalVerdict: Verdict,
    val avgThreatScore: Int?,
    val hashValues: HashValues,
    val analyses: List<Analysis>
)