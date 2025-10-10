package com.openlysis.feature.tools

import androidx.compose.runtime.Immutable
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.MessageData

/**
 * UI state for the SMS Analysis Tool.
 *
 * @property message Current SMS message state.
 * @property canRequestAnalysis Indicates if analysis can be requested based on message state.
 * @property requestState Current state of the analysis request.
 */
@Immutable
internal data class SmsAnalysisToolUiState(
    val message: MessageData = MessageData(),
    val canRequestAnalysis: Boolean = message.requiredFieldsSatisfied,
    val requestState: AnalysisRequestState = AnalysisRequestState.None
)