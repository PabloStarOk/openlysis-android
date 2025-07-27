package com.openlysis.feature.tools

import androidx.compose.runtime.Immutable
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.AttachedFileData
import com.openlysis.feature.tools.model.MessageData

/**
 * UI state for the Email Analysis Tool feature.
 *
 * @property message Current message state for the tool.
 * @property attachedFiles Map of attached files, keyed by an integer value.
 * @property canRequestAnalysis Indicates if analysis can be requested.
 * @property canAttachFiles Indicates if files can be attached.
 * @property requestState Current state of the analysis request.
 */
@Immutable
internal data class EmailAnalysisToolUiState(
    val message: MessageData = MessageData(),
    val attachedFiles: Map<Int, AttachedFileData> = emptyMap(),
    val canRequestAnalysis: Boolean = false,
    val canAttachFiles: Boolean = true,
    val requestState: AnalysisRequestState = AnalysisRequestState.None
)