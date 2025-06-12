package com.openlysis.data.api.dto.message

import com.openlysis.models.analysis.AnalysisStatus
import com.openlysis.models.common.Verdict
import com.openlysis.models.message.MessageAnalysis
import com.squareup.moshi.JsonClass
import java.time.Instant

/**
 * Data Transfer Object (DTO) for the [MessageAnalysis] model.
 *
 * @property id Unique identifier for the analysis.
 * @property startedDate ISO-8601 formatted date when the analysis started.
 * @property messageInformation Information about the message being analyzed.
 * @property status Status of the analysis as a string.
 * @property verdict Verdict of the analysis as a string.
 * @property results Results of the message analysis.
 */
@JsonClass(generateAdapter = true)
internal data class MessageAnalysisDto(
    val id: String,
    val startedDate: String,
    val messageInformation: MessageInformationDto,
    val status: String,
    val verdict: String,
    val results: MessageAnalysisResultsDto
) {
    /**
     * Converts this DTO to the domain model [MessageAnalysis].
     *
     * @return [MessageAnalysis] domain model instance.
     */
    internal fun convertToModel(): MessageAnalysis =
        MessageAnalysis(
            id = id,
            startedDate = Instant.parse(startedDate),
            message = messageInformation.convertToModel(),
            hashValues = messageInformation.messageHashValues,
            status = AnalysisStatus.parse(status),
            verdict = Verdict.parse(verdict),
            urlMultiAnalyses = results.urlMultiAnalyses.map { a -> a.convertToModel() },
            fileMultiAnalyses = results.fileMultiAnalyses.map { a -> a.convertToModel() },
            emailAddressMultiReputations =
                results.emailAddressMultiReputations.map { r ->
                    r.convertToModel()
                },
            phoneNumberMultiReputations =
                results.phoneNumberMultiReputations.map { r ->
                    r.convertToModel()
                }
        )
}