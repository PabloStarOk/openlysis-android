package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openlysis.data.analysis.model.analysis.Analysis

/**
 * Entity representing a single analysis result for a file.
 *
 * @property id Unique identifier for the analysis.
 * @property columns The [AnalysisColumns] containing analysis details and parent reference.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            FileMultiAnalysisEntity::class,
            parentColumns = ["id"],
            childColumns = ["multiAnalysisId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class FileAnalysisEntity(
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
         * Creates a [FileAnalysisEntity] from a [Analysis] model and parent multi-analysis ID.
         *
         * @param model The [Analysis] model.
         * @param multiAnalysisId The parent multi-analysis ID.
         * @return The [FileAnalysisEntity] entity.
         */
        fun createFromModel(
            model: Analysis,
            multiAnalysisId: String
        ) = FileAnalysisEntity(
            id = model.id,
            columns = AnalysisColumns.createFromModel(model, multiAnalysisId)
        )
    }
}