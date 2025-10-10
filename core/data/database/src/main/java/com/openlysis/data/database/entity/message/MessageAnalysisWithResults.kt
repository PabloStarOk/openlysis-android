package com.openlysis.data.database.entity.message

import androidx.room.Embedded
import androidx.room.Relation
import com.openlysis.data.database.entity.analysis.FileMultiAnalysisEntity
import com.openlysis.data.database.entity.analysis.UrlMultiAnalysisEntity
import com.openlysis.data.database.entity.reputation.MultiReputationEntity

/**
 * Data class representing a [MessageAnalysisEntity] and its related analysis results.
 *
 * @property messageAnalysis The [MessageAnalysisEntity] being analyzed.
 * @property urlMultiAnalyses List of related [UrlMultiAnalysisEntity] records.
 * @property fileMultiAnalyses List of related [FileMultiAnalysisEntity] records.
 * @property multiReputations List of related [MultiReputationEntity] records.
 */
internal data class MessageAnalysisWithResults(
    @Embedded val messageAnalysis: MessageAnalysisEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageAnalysisId"
    )
    val urlMultiAnalyses: List<UrlMultiAnalysisEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageAnalysisId"
    )
    val fileMultiAnalyses: List<FileMultiAnalysisEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageAnalysisId"
    )
    val multiReputations: List<MultiReputationEntity>
)