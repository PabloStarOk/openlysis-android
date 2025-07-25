package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.tools.components.MessageSection
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.data.AnalysisRequestState

/**
 * Composable screen for SMS analysis tool.
 *
 * @param viewModel The view model handling the business logic for the tools screens
 * @param onAnalysisStart Callback invoked when message analysis starts, providing the initial [MessageAnalysis] result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Optional modifier for the screen's layout customization
 */
@Composable
internal fun SmsAnalysisToolScreen(
    viewModel: SmsAnalysisToolScreenViewModel,
    onAnalysisStart: (MessageAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val requestState = uiState.requestState

    ToolScreenScaffold(
        onSubmitRequest = viewModel::startAnalysis,
        onGoToAnalysisRequest = {
            if (requestState is AnalysisRequestState.Success.Message) {
                onAnalysisStart(requestState.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelRequest,
        analysisRequestState = requestState,
        screenTitle = stringResource(R.string.sms_message_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = uiState.canRequestAnalysis,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an SMS from the device's inbox.
        MessageSection(
            state = uiState.message,
            onSenderChange = viewModel::updateSender,
            onContentChange = viewModel::updateContent,
            title = stringResource(R.string.sms_message_tool_section_title),
            description = stringResource(R.string.sms_message_tool_section_description),
            isEmail = false
        )
    }
}