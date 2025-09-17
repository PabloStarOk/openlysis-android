package com.openlysis.feature.results.components.preview

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
 * @property isRefreshing If the preview is being refreshed.
 */
internal data class AnalysisPreviewState(
    val id: String,
    val headerContent: String,
    val startedDate: Instant,
    val status: AnalysisStatus,
    val verdict: Verdict,
    val isRefreshing: Boolean = false
) {
    /**
     * Checks if the analysis is in a state that can be refreshed (i.e., not a final state).
     */
    fun isRefreshable(): Boolean =
        status == AnalysisStatus.Queued || status == AnalysisStatus.InProgress
}