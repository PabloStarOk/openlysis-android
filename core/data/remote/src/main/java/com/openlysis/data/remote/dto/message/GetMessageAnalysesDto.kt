package com.openlysis.data.remote.dto.message

/**
 * DTO containing a collection of [MessageAnalysisDto] of the user.
 *
 * @param analyses List of message analysis results.
 */

internal data class GetMessageAnalysesDto(
    val analyses: List<MessageAnalysisDto>
)