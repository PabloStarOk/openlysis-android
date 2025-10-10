package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.MultiAnalysis
import com.openlysis.data.analysis.model.common.HashValues
import com.openlysis.data.analysis.model.common.Verdict
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Shared entity columns representing a multi-analysis record for a file or URL.
 *
 * @property startedDate The date and time when the multi-analysis started.
 * @property status The [AnalysisStatus] of the multi-analysis.
 * @property finalVerdict The final [Verdict] of the multi-analysis.
 * @property avgThreatScore The average threat score across all analyses, if available.
 * @property hashValues The [HashValues] associated with the analyzed object.
 * @property messageAnalysisId The ID of the parent message analysis, if any.
 * @property createdAt The timestamp when this entity was created in the database.
 * @property hasParent Whether this multi-analysis has a parent message analysis.
 */
internal data class MultiAnalysisColumns(
    val startedDate: Instant,
    val status: AnalysisStatus,
    val finalVerdict: Verdict,
    val avgThreatScore: Int?,
    @Embedded("hash_") val hashValues: HashValues,
    val messageAnalysisId: String?,
    val createdAt: Instant,
    val hasParent: Boolean
) {
    companion object {
        /**
         * Creates a [MultiAnalysisColumns] entity from a [MultiAnalysis] model and parent message analysis ID.
         *
         * @param model The [MultiAnalysis] model.
         * @param messageAnalysisId The parent message analysis ID, if any.
         * @return The [MultiAnalysisColumns] entity.
         */
        fun createFromModel(
            model: MultiAnalysis,
            messageAnalysisId: String?
        ): MultiAnalysisColumns =
            MultiAnalysisColumns(
                startedDate = model.startedDate,
                status = model.status,
                finalVerdict = model.finalVerdict,
                avgThreatScore = model.avgThreatScore,
                hashValues = model.hashValues,
                messageAnalysisId = messageAnalysisId,
                createdAt = Clock.System.now(),
                hasParent = messageAnalysisId != null
            )
    }
}