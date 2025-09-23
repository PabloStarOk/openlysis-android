package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.openlysis.data.analysis.model.analysis.Analysis

/**
 * Entity representing a single analysis result for a URL.
 *
 * @property columns The [AnalysisColumns] containing analysis details and parent reference.
 */
@Entity(
    primaryKeys = ["multiAnalysisId", "id"],
    foreignKeys = [
        ForeignKey(
            UrlMultiAnalysisEntity::class,
            parentColumns = ["id"],
            childColumns = ["multiAnalysisId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("multiAnalysisId")]
)
internal data class UrlAnalysisEntity(
    @Embedded val columns: AnalysisColumns
) {
    /**
     * Converts this entity to a [Analysis] model.
     *
     * @return The [Analysis] model.
     */
    fun convertToModel(): Analysis = columns.convertToModel()

    companion object {
        /**
         * Creates a [UrlAnalysisEntity] from a [Analysis] model and parent multi-analysis ID.
         *
         * @param model The [Analysis] model.
         * @param multiAnalysisId The parent multi-analysis ID.
         * @return The [UrlAnalysisEntity] entity.
         */
        fun createFromModel(
            model: Analysis,
            multiAnalysisId: String
        ) = UrlAnalysisEntity(
            columns = AnalysisColumns.createFromModel(model, multiAnalysisId)
        )
    }
}