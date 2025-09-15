package com.openlysis.data.remote.dto.analysis

import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMetadata
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.common.HashValues
import com.openlysis.data.analysis.model.common.Verdict
import com.squareup.moshi.JsonClass
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant

/**
 * Data Transfer Object (DTO) for the [FileMultiAnalysis] model.
 *
 * @property id Unique identifier for the analysis.
 * @property startedDate ISO-8601 formatted date string when the analysis started.
 * @property status Status of the analysis as a string.
 * @property finalVerdict Final verdict of the analysis as a string.
 * @property averageThreatScore Optional average threat score for the file.
 * @property fileHashValues Hash values associated with the file.
 * @property analyses List of individual analysis DTOs.
 * @property fileMetadata Metadata about the analyzed file.
 */
@JsonClass(generateAdapter = true)
internal data class FileMultiAnalysisDto(
    val id: String,
    val startedDate: String,
    val status: String,
    val finalVerdict: String,
    val averageThreatScore: Int?,
    val fileHashValues: HashValues,
    val analyses: List<AnalysisDto>,
    val fileMetadata: FileMetadata
) {
    /**
     * Converts this DTO to the domain model [FileMultiAnalysis].
     *
     * @return [FileMultiAnalysis] domain model instance.
     */
    internal fun convertToModel(): FileMultiAnalysis =
        FileMultiAnalysis(
            id = id,
            startedDate = Instant.parse(startedDate).toJavaInstant(),
            status = AnalysisStatus.parse(status),
            finalVerdict = Verdict.parse(finalVerdict),
            avgThreatScore = averageThreatScore,
            hashValues = fileHashValues,
            analyses = analyses.map { s -> s.convertToModel() },
            fileMetadata = fileMetadata
        )
}