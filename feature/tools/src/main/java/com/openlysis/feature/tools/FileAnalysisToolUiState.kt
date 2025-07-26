package com.openlysis.feature.tools

import androidx.compose.runtime.Immutable
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.AttachedFileData

/**
 * UI state for the File Analysis Tool.
 *
 * @property file The currently attached file, or null if none.
 * @property isFileAttached True if a file is attached.
 * @property canRequestAnalysis True if analysis can be requested (file is attached and has no error).
 * @property requestState The current state of the analysis request.
 */
@Immutable
internal data class FileAnalysisToolUiState(
    val file: AttachedFileData? = null,
    val isFileAttached: Boolean = file != null,
    val canRequestAnalysis: Boolean = isFileAttached && file?.error == null,
    val requestState: AnalysisRequestState = AnalysisRequestState.None
)