package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.analysis.Analysis
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict

/**
 * DTO sent over SignalR for [Analysis] model.
 *
 * @property id Unique identifier for the analysis.
 * @property serviceName Name of the service performing the analysis.
 * @property status Current status of the analysis.
 * @property verdict Final verdict of the analysis.
 * @property threatScore Optional threat score associated with the analysis.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class AnalysisDto
    @JsonCreator
    constructor(
        @JsonProperty("Id") val id: String,
        @JsonProperty("ServiceName") val serviceName: String,
        @JsonProperty("Status") val status: AnalysisStatus,
        @JsonProperty("Verdict") val verdict: Verdict,
        @JsonProperty("ThreatScore") val threatScore: Int?
    ) {
        /**
         * Converts this DTO to the domain model [Analysis].
         *
         * @return [Analysis] instance with data from this DTO.
         */
        internal fun convertToModel(): Analysis =
            Analysis(
                id = id,
                serviceName = serviceName,
                status = status,
                verdict = verdict,
                threatScore = threatScore
            )
    }