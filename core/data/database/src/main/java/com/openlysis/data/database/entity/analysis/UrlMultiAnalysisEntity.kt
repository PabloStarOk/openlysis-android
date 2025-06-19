package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openlysis.data.analysis.model.analysis.Analysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.database.entity.message.MessageAnalysisEntity
import java.net.URI

/**
 * Entity representing a multi-analysis result for a URL.
 *
 * @property id Unique identifier for the multi-analysis.
 * @property columns The [MultiAnalysisColumns] containing multi-analysis details and parent reference.
 * @property url The [URI] being analyzed.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            MessageAnalysisEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageAnalysisId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class UrlMultiAnalysisEntity(
    @PrimaryKey val id: String,
    @Embedded val columns: MultiAnalysisColumns,
    val url: URI
) {
    /**
     * Converts this entity to a [UrlMultiAnalysis] model.
     *
     * @param analyses The list of [Analysis] results for this URL.
     * @return The [UrlMultiAnalysis] model.
     */
    fun convertToModel(analyses: List<Analysis>): UrlMultiAnalysis =
        UrlMultiAnalysis(
            id = id,
            startedDate = columns.startedDate,
            status = columns.status,
            finalVerdict = columns.finalVerdict,
            avgThreatScore = columns.avgThreatScore,
            hashValues = columns.hashValues,
            analyses = analyses,
            url = url
        )

    companion object {
        /**
         * Creates a [UrlMultiAnalysisEntity] from a [UrlMultiAnalysis] model and parent message analysis ID.
         *
         * @param model The [UrlMultiAnalysis] model.
         * @param messageAnalysisId The parent message analysis ID, if any.
         * @return The [UrlMultiAnalysisEntity] entity.
         */
        fun createFromModel(
            model: UrlMultiAnalysis,
            messageAnalysisId: String?
        ): UrlMultiAnalysisEntity =
            UrlMultiAnalysisEntity(
                id = model.id,
                columns = MultiAnalysisColumns.createFromModel(model, messageAnalysisId),
                url = model.url
            )
    }
}