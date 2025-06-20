package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import androidx.room.Relation
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.database.entity.BuildablePojo

/**
 * Data class representing a [FileMultiAnalysisEntity] and its related file analyses.
 *
 * @property multiAnalysis The [FileMultiAnalysisEntity] being referenced.
 * @property analyses List of related [FileAnalysisEntity] records.
 */
internal data class FileMultiAnalysisWithAnalyses(
    @Embedded val multiAnalysis: FileMultiAnalysisEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "multiAnalysisId"
    )
    val analyses: List<FileAnalysisEntity>
) : BuildablePojo<FileMultiAnalysis> {
    /**
     * Builds a [FileMultiAnalysis] model from this entity and its analyses.
     *
     * @return The [FileMultiAnalysis] model.
     */
    override fun buildModel(): FileMultiAnalysis =
        multiAnalysis.convertToModel(
            analyses = analyses.map { a -> a.convertToModel() }
        )
}