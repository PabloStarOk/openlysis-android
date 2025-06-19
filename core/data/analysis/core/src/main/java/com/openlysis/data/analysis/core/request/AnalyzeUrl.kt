package com.openlysis.data.analysis.core.request

import java.net.URI

/**
 * A request to analyze a URL.
 *
 * @property url The URL to be analyzed.
 * @property reanalyze Whether the URL must be analyzed or it should try to fetch an existing analysis first
 */
data class AnalyzeUrl(
    val url: URI,
    val reanalyze: Boolean
)