package com.openlysis.data.remote.dto.message

import com.openlysis.data.remote.dto.analysis.FileMultiAnalysisDto
import com.openlysis.data.remote.dto.analysis.UrlMultiAnalysisDto
import com.openlysis.data.remote.dto.reputation.EmailAddressMultiReputationDto
import com.openlysis.data.remote.dto.reputation.PhoneNumberMultiReputationDto
import com.squareup.moshi.JsonClass

/**
 * DTO representing the results of a [MessageAnalysisDto].
 *
 * @property fileMultiAnalyses List of file analysis results.
 * @property urlMultiAnalyses List of URL analysis results.
 * @property emailAddressMultiReputations List of email address reputation results.
 * @property phoneNumberMultiReputations List of phone number reputation results.
 */
@JsonClass(generateAdapter = true)
internal data class MessageAnalysisResultsDto(
    val fileMultiAnalyses: List<FileMultiAnalysisDto>,
    val urlMultiAnalyses: List<UrlMultiAnalysisDto>,
    val emailAddressMultiReputations: List<EmailAddressMultiReputationDto>,
    val phoneNumberMultiReputations: List<PhoneNumberMultiReputationDto>
)