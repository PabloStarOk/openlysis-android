package com.openlysis.feature.tools

import com.openlysis.feature.tools.components.MessageState
import com.openlysis.feature.tools.data.AnalysisRequestState

/**
 * UI state for the SMS Analysis Tool.
 *
 * @property message Current SMS message state.
 * @property canRequestAnalysis Indicates if analysis can be requested based on message state.
 * @property requestState Current state of the analysis request.
 */
internal data class SmsAnalysisToolUiState(
    val message: MessageState = MessageState(),
    val canRequestAnalysis: Boolean = message.submitEnabled,
    val requestState: AnalysisRequestState = AnalysisRequestState.None
)