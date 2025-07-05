package com.openlysis.data.remote.dto.message

import com.squareup.moshi.JsonClass

/**
 * DTO containing a collection of [MessageAnalysisDto] of the user.
 *
 * @param analyses List of message analysis results.
 */
@JsonClass(generateAdapter = true)
internal data class GetMessageAnalysesDto(
    val analyses: List<MessageAnalysisDto>
)