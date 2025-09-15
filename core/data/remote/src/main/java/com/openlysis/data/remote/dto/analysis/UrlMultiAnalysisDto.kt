package com.openlysis.data.remote.dto.analysis

import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.HashValues
import com.openlysis.data.analysis.model.common.Verdict
import com.squareup.moshi.JsonClass
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.net.URI

/**
 * Data Transfer Object (DTO) for the [UrlMultiAnalysis] model.
 *
 * @property id Unique identifier for the analysis.
 * @property startedDate ISO-8601 formatted date when the analysis started.
 * @property status Status of the analysis as a string.
 * @property finalVerdict Final verdict of the analysis as a string.
 * @property averageThreatScore Optional average threat score.
 * @property urlHashValues Hash values associated with the URL.
 * @property analyses List of individual analysis DTOs.
 * @property url The analyzed URL as a string.
 */
@JsonClass(generateAdapter = true)
internal data class UrlMultiAnalysisDto(
    val id: String,
    val startedDate: String,
    val status: String,
    val finalVerdict: String,
    val averageThreatScore: Int?,
    val urlHashValues: HashValues,
    val analyses: List<AnalysisDto>,
    val url: String
) {
    /**
     * Converts this DTO to the domain model [UrlMultiAnalysis].
     *
     * @return [UrlMultiAnalysis] domain model instance.
     */
    internal fun convertToModel(): UrlMultiAnalysis =
        UrlMultiAnalysis(
            id = id,
            startedDate = Instant.parse(startedDate).toJavaInstant(),
            status = AnalysisStatus.parse(status),
            finalVerdict = Verdict.parse(finalVerdict),
            avgThreatScore = averageThreatScore,
            hashValues = urlHashValues,
            analyses = analyses.map { a -> a.convertToModel() },
            url = URI(url)
        )
}