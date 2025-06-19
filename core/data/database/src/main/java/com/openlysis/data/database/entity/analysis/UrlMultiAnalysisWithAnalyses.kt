package com.openlysis.data.database.entity.analysis

import androidx.room.Embedded
import androidx.room.Relation
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis

/**
 * Data class representing a [UrlMultiAnalysisEntity] and its related URL analyses.
 *
 * @property multiAnalysis The [UrlMultiAnalysisEntity] being referenced.
 * @property analyses List of related [UrlAnalysisEntity] records.
 */
internal data class UrlMultiAnalysisWithAnalyses(
    @Embedded val multiAnalysis: UrlMultiAnalysisEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "multiAnalysisId"
    )
    val analyses: List<UrlAnalysisEntity>
) {
    /**
     * Builds a [UrlMultiAnalysis] model from this entity and its analyses.
     *
     * @return The [UrlMultiAnalysis] model.
     */
    fun buildMultiAnalysis(): UrlMultiAnalysis =
        multiAnalysis.convertToModel(
            analyses = analyses.map { a -> a.convertToModel() }
        )
}