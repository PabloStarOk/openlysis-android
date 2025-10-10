package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.Verdict
import kotlinx.datetime.Instant
import java.net.URI

/**
 * DTO sent over SignalR for [UrlMultiAnalysis] model.
 *
 * @property id Unique identifier for the analysis.
 * @property startedDateMillis Start date of the analysis in milliseconds since epoch.
 * @property status Current status of the analysis.
 * @property finalVerdict Final verdict of the analysis.
 * @property averageThreatScore Average threat score across analyses.
 * @property url The URL being analyzed.
 * @property urlHashValues Hash values for the URL.
 * @property analyses List of individual analysis DTOs.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class UrlMultiAnalysisDto
    @JsonCreator
    constructor(
        @JsonProperty("Id")
        override val id: String,
        @JsonProperty("StartedDate")
        override val startedDateMillis: Long,
        @JsonProperty("Status")
        override val status: AnalysisStatus,
        @JsonProperty("FinalVerdict")
        val finalVerdict: Verdict,
        @JsonProperty("AverageThreatScore")
        val averageThreatScore: Int?,
        @JsonProperty("Url")
        val url: String,
        @JsonProperty("UrlHashValues")
        val urlHashValues: HashValuesDto,
        @JsonProperty("Analyses")
        val analyses: List<AnalysisDto>
    ) : BaseAnalysisDto<UrlMultiAnalysis> {
        /**
         * Converts this DTO to the domain model [UrlMultiAnalysis].
         *
         * @return [UrlMultiAnalysis] domain model instance.
         */
        override fun convertToModel(): UrlMultiAnalysis =
            UrlMultiAnalysis(
                id = id,
                startedDate = Instant.fromEpochMilliseconds(startedDateMillis),
                status = status,
                finalVerdict = finalVerdict,
                avgThreatScore = averageThreatScore,
                hashValues = urlHashValues.convertToModel(),
                analyses = analyses.map { it.convertToModel() },
                url = URI(url)
            )
    }