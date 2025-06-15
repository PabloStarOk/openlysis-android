package com.openlysis.data.local.entity.analysis

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openlysis.data.local.entity.message.MessageAnalysisEntity
import com.openlysis.models.analysis.Analysis
import com.openlysis.models.analysis.FileMetadata
import com.openlysis.models.analysis.FileMultiAnalysis

/**
 * Entity representing a multi-analysis result for a file, including file metadata.
 *
 * @property id Unique identifier for the multi-analysis.
 * @property columns The [MultiAnalysisColumns] containing multi-analysis details and parent reference.
 * @property fileMetadata The [FileMetadata] of the analyzed file.
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
internal data class FileMultiAnalysisEntity(
    @PrimaryKey val id: String,
    @Embedded val columns: MultiAnalysisColumns,
    @Embedded(prefix = "file_") val fileMetadata: FileMetadata
) {
    /**
     * Converts this entity to a [FileMultiAnalysis] model.
     *
     * @param analyses The list of [Analysis] results for this file.
     * @return The [FileMultiAnalysis] model.
     */
    fun convertToModel(analyses: List<Analysis>): FileMultiAnalysis =
        FileMultiAnalysis(
            id = id,
            startedDate = columns.startedDate,
            status = columns.status,
            finalVerdict = columns.finalVerdict,
            avgThreatScore = columns.avgThreatScore,
            hashValues = columns.hashValues,
            analyses = analyses,
            fileMetadata = fileMetadata
        )

    companion object {
        /**
         * Creates a [FileMultiAnalysisEntity] from a [FileMultiAnalysis] model and parent message analysis ID.
         *
         * @param model The [FileMultiAnalysis] model.
         * @param messageAnalysisId The parent message analysis ID, if any.
         * @return The [FileMultiAnalysisEntity] entity.
         */
        fun createFromModel(
            model: FileMultiAnalysis,
            messageAnalysisId: String?
        ): FileMultiAnalysisEntity =
            FileMultiAnalysisEntity(
                id = model.id,
                columns = MultiAnalysisColumns.createFromModel(model, messageAnalysisId),
                fileMetadata = model.fileMetadata
            )
    }
}