package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * DTO sent over SignalR for message analysis results model.
 *
 * @property fileMultiAnalyses List of file multi-analysis DTOs.
 * @property urlMultiAnalyses List of URL multi-analysis DTOs.
 * @property emailAddressMultiReputations List of email address multi-reputation DTOs.
 * @property phoneNumberMultiReputations List of phone number multi-reputation DTOs.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class MessageAnalysisResultsDto
    @JsonCreator
    constructor(
        @JsonProperty("FileMultiAnalyses")
        val fileMultiAnalyses: List<FileMultiAnalysisDto>,
        @JsonProperty("UrlMultiAnalyses")
        val urlMultiAnalyses: List<UrlMultiAnalysisDto>,
        @JsonProperty("EmailAddressMultiReputations")
        val emailAddressMultiReputations: List<EmailAddressMultiReputationDto>,
        @JsonProperty("PhoneNumberMultiReputations")
        val phoneNumberMultiReputations: List<PhoneNumberMultiReputationDto>
    )