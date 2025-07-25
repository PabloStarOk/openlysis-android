package com.openlysis.feature.tools

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.openlysis.feature.tools.components.MessageState
import com.openlysis.feature.tools.data.AnalysisRequestState
import com.openlysis.feature.tools.data.AttachedFileData

/**
 * UI state for the Email Analysis Tool feature.
 *
 * @property message Current message state for the tool.
 * @property attachedFiles Map of attached files, keyed by their Uri.
 * @property invalidAttachedFiles Set of files that failed validation.
 * @property canRequestAnalysis Indicates if analysis can be requested.
 * @property canAttachFiles Indicates if files can be attached.
 * @property requestState Current state of the analysis request.
 */
@Immutable
internal data class EmailAnalysisToolUiState(
    val message: MessageState = MessageState(),
    val attachedFiles: Map<Uri, AttachedFileData> = emptyMap(),
    val invalidAttachedFiles: Set<AttachedFileData> = emptySet(),
    val canRequestAnalysis: Boolean = false,
    val canAttachFiles: Boolean = true,
    val requestState: AnalysisRequestState = AnalysisRequestState.None
)