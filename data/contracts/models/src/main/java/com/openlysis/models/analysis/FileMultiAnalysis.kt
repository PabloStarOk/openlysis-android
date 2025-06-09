package com.openlysis.models.analysis

import com.openlysis.models.common.Verdict
import java.time.Instant

/**
 * The result of a multi-analysis performed on a file.
 *
 * @property id A unique identifier for this multi-analysis.
 * @property startedDate The exact moment when the multi-analysis process was initiated.
 * @property status The current overall [AnalysisStatus] of the multi-analysis.
 * @property finalVerdict The conclusive [Verdict] of the analyzed file based on the combined results of all individual analyses.
 * @property avgThreatScore An optional average threat score calculated from the threat scores of the individual analyses. This can be null if not applicable or if no individual analyses provided a score.
 * @property hashValues A [HashValues] object calculated for the analyzed file or artifact.
 * @property analyses A list of [Analysis] objects.
 * @property fileMetadata A [FileMetadata] object associated to the analyzed file.
 */
class FileMultiAnalysis(
    id: String,
    startedDate: Instant,
    status: AnalysisStatus,
    finalVerdict: Verdict,
    avgThreatScore: Int?,
    hashValues: HashValues,
    analyses: List<Analysis>,
    val fileMetadata: FileMetadata
) : MultiAnalysis(id, startedDate, status, finalVerdict, avgThreatScore, hashValues, analyses)