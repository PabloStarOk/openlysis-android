package com.openlysis.data.database.entity.analysis

import com.openlysis.data.analysis.model.analysis.Analysis
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict

/**
 * Shared entity columns representing a single analysis result for a file or URL.
 *
 * @property id The ID of the analysis.
 * @property serviceName The name of the analysis service.
 * @property status The [AnalysisStatus] of the analysis.
 * @property verdict The [Verdict] of the analysis.
 * @property threatScore The threat score assigned by the service, if available.
 * @property multiAnalysisId The ID of the parent multi-analysis entity.
 */
internal data class AnalysisColumns(
    val id: String,
    val serviceName: String,
    val status: AnalysisStatus,
    val verdict: Verdict,
    val threatScore: Int?,
    val multiAnalysisId: String
) {
    /**
     * Converts this entity to a [Analysis] model.
     *
     * @return The [Analysis] model.
     */
    fun convertToModel(): Analysis =
        Analysis(
            id = id,
            serviceName = serviceName,
            status = status,
            verdict = verdict,
            threatScore = threatScore
        )

    companion object {
        /**
         * Creates an [AnalysisColumns] entity from a [Analysis] model and parent multi-analysis ID.
         *
         * @param model The [Analysis] model.
         * @param multiAnalysisId The parent multi-analysis ID.
         * @return The [AnalysisColumns] entity.
         */
        fun createFromModel(
            model: Analysis,
            multiAnalysisId: String
        ): AnalysisColumns =
            AnalysisColumns(
                id = model.id,
                serviceName = model.serviceName,
                status = model.status,
                verdict = model.verdict,
                threatScore = model.threatScore,
                multiAnalysisId = multiAnalysisId
            )
    }
}