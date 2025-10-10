package com.openlysis.feature.tools

import androidx.compose.runtime.Immutable
import com.openlysis.feature.tools.model.AnalysisRequestState

/**
 * UI state for the URL Analysis Tool.
 *
 * @property url The URL entered by the user.
 * @property isValidUrl Indicates if the entered URL is valid.
 * @property canRequestAnalysis True if analysis can be requested, based on URL validity.
 * @property requestState Current state of the analysis request.
 */
@Immutable
internal data class UrlAnalysisToolUiState(
    val url: String = "",
    val isValidUrl: Boolean = false,
    val canRequestAnalysis: Boolean = isValidUrl,
    val requestState: AnalysisRequestState = AnalysisRequestState.None
)