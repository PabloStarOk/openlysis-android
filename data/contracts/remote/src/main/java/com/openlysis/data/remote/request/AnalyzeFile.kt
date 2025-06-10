package com.openlysis.data.remote.request

import java.io.File

/**
 * A request to analyze a file.
 *
 * @property file The file to be analyzed.
 * @property password An optional password to unlock the file.
 * @property reanalyze Whether the file must be analyzed or it should try to fetch an existing analysis first.
 */
data class AnalyzeFile(
    val file: File,
    val password: String?,
    val reanalyze: Boolean
)