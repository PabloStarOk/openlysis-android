package com.openlysis.data.analysis.core.request

/**
 * A request to analyze a file.
 *
 * @property attachment An [Attachment] object.
 * @property reanalyze Whether the file must be analyzed or it should try to fetch an existing analysis first.
 */
data class AnalyzeFile(
    val attachment: Attachment,
    val reanalyze: Boolean
)