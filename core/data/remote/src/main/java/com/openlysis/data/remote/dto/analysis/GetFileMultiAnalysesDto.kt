package com.openlysis.data.remote.dto.analysis

import com.squareup.moshi.JsonClass

/**
 * DTO containing a collection of [FileMultiAnalysisDto] of the user.
 *
 * @param analyses List of file analysis results.
 */
@JsonClass(generateAdapter = true)
internal data class GetFileMultiAnalysesDto(
    val analyses: List<FileMultiAnalysisDto>
)