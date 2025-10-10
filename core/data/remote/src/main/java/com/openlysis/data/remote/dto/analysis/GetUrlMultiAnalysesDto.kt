package com.openlysis.data.remote.dto.analysis

import com.squareup.moshi.JsonClass

/**
 * DTO containing a collection of [UrlMultiAnalysisDto] of the user.
 *
 * @param analyses List of URL analysis results.
 */
@JsonClass(generateAdapter = true)
internal data class GetUrlMultiAnalysesDto(
    val analyses: List<UrlMultiAnalysisDto>
)