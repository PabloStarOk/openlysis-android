package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import kotlinx.datetime.Instant

/**
 * DTO sent over SignalR for [MessageAnalysis] model.
 *
 * @property id Unique identifier for the analysis.
 * @property startedDateMillis Timestamp in milliseconds when the analysis started.
 * @property messageInformation Information about the analyzed message.
 * @property status Current status of the analysis.
 * @property verdict Final verdict of the analysis.
 * @property results Analysis results including URLs, files, email and phone reputations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class MessageAnalysisDto
    @JsonCreator
    constructor(
        @JsonProperty("Id")
        override val id: String,
        @JsonProperty("StartedDate")
        override val startedDateMillis: Long,
        @JsonProperty("MessageInformation")
        val messageInformation: MessageInformationDto,
        @JsonProperty("Status")
        override val status: AnalysisStatus,
        @JsonProperty("Verdict")
        val verdict: Verdict,
        @JsonProperty("Results")
        val results: MessageAnalysisResultsDto
    ) : BaseAnalysisDto<MessageAnalysis> {
        /**
         * Converts this DTO to the domain model [MessageAnalysis].
         *
         * @return [MessageAnalysis] domain model instance.
         */
        override fun convertToModel(): MessageAnalysis =
            MessageAnalysis(
                id = id,
                startedDate = Instant.fromEpochMilliseconds(startedDateMillis),
                message = messageInformation.convertToModel(),
                hashValues = messageInformation.hashValues.convertToModel(),
                status = status,
                verdict = verdict,
                urlMultiAnalyses = results.urlMultiAnalyses.map { it.convertToModel() },
                fileMultiAnalyses = results.fileMultiAnalyses.map { it.convertToModel() },
                emailAddressMultiReputations =
                    results.emailAddressMultiReputations.map { it.convertToModel() },
                phoneNumberMultiReputations =
                    results.phoneNumberMultiReputations.map { it.convertToModel() }
            )
    }