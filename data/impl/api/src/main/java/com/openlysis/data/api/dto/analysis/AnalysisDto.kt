package com.openlysis.data.api.dto.analysis

import com.openlysis.models.analysis.Analysis
import com.openlysis.models.analysis.AnalysisStatus
import com.openlysis.models.common.Verdict
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object (DTO) for the [Analysis] model.
 *
 * @property serviceName Name of the analysis service.
 * @property status String representation of the analysis status.
 * @property verdict String representation of the analysis verdict.
 * @property threatScore Optional threat score assigned by the analysis.
 */
@JsonClass(generateAdapter = true)
internal data class AnalysisDto(
    val id: String,
    val serviceName: String,
    val status: String,
    val verdict: String,
    val threatScore: Int?
) {
    /**
     * Converts this DTO to the domain [Analysis] model.
     *
     * @return [Analysis] instance with parsed status and verdict.
     */
    internal fun convertToModel(): Analysis =
        Analysis(
            id = id,
            serviceName = serviceName,
            status = AnalysisStatus.parse(status),
            verdict = Verdict.parse(verdict),
            threatScore = threatScore
        )
}