package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.common.Verdict
import kotlinx.datetime.Instant

/**
 * DTO sent over SignalR for [FileMultiAnalysis] model.
 *
 * @property id Unique identifier of the analysis.
 * @property startedDateMillis Start date of the analysis in milliseconds since epoch.
 * @property status Current status of the analysis.
 * @property finalVerdict Final verdict of the analysis.
 * @property averageThreatScore Average threat score across analyses.
 * @property fileMetadata Metadata of the analyzed file.
 * @property fileHashValues Hash values of the analyzed file.
 * @property analyses List of individual analysis DTOs.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class FileMultiAnalysisDto
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
        @JsonProperty("FileMetadata")
        val fileMetadata: FileMetadataDto,
        @JsonProperty("FileHashValues")
        val fileHashValues: HashValuesDto,
        @JsonProperty("Analyses")
        val analyses: List<AnalysisDto>
    ) : BaseAnalysisDto<FileMultiAnalysis> {
        /**
         * Converts this DTO to the domain model [FileMultiAnalysis].
         *
         * @return [FileMultiAnalysis] domain model instance.
         */
        override fun convertToModel(): FileMultiAnalysis =
            FileMultiAnalysis(
                id = id,
                startedDate = Instant.fromEpochMilliseconds(startedDateMillis),
                status = status,
                finalVerdict = finalVerdict,
                avgThreatScore = averageThreatScore,
                hashValues = fileHashValues.convertToModel(),
                analyses = analyses.map { it.convertToModel() },
                fileMetadata = fileMetadata.convertToModel()
            )
    }