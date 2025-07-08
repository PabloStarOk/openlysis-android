package com.openlysis.feature.results.components

import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import java.time.Instant

/**
 * Represents the state of an analysis preview.
 *
 * @property id Unique identifier of the analysis
 * @property headerContent Content displayed in the header of the analysis preview
 * @property startedDate Timestamp when the analysis was initiated
 * @property status Current status of the analysis
 * @property verdict Verdict of the analysis
 */
internal data class AnalysisPreviewState(
    val id: String,
    val headerContent: String,
    val startedDate: Instant,
    val status: AnalysisStatus,
    val verdict: Verdict
)