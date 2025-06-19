package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openlysis.data.analysis.model.analysis.Analysis

/**
 * Entity representing a single analysis result for a URL.
 *
 * @property id Unique identifier for the analysis.
 * @property columns The [AnalysisColumns] containing analysis details and parent reference.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            UrlMultiAnalysisEntity::class,
            parentColumns = ["id"],
            childColumns = ["multiAnalysisId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class UrlAnalysisEntity(
    @PrimaryKey val id: String,
    @Embedded val columns: AnalysisColumns
) {
    /**
     * Converts this entity to a [Analysis] model.
     *
     * @return The [Analysis] model.
     */
    fun convertToModel(): Analysis = columns.convertToModel(id)

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
            id = model.id,
            columns = AnalysisColumns.createFromModel(model, multiAnalysisId)
        )
    }
}