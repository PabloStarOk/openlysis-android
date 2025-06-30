package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.tools.components.MessageSection
import com.openlysis.feature.tools.components.MessageState
import com.openlysis.feature.tools.components.ToolScreenScaffold

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
    viewModel: ToolsScreenViewModel,
    onAnalysisStart: (MessageAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messageUiNotifier = remember { MessageUiNotifier(context) }
    val errorHandler = remember { AnalysisErrorUiHandler(messageUiNotifier) }
    val state = rememberSaveable { mutableStateOf(MessageState()) }
    val submitEnabled by
        remember(state) {
            derivedStateOf {
                state.value.submitEnabled
            }
        }

    ToolScreenScaffold(
        onSubmitClick = {
            viewModel.startMessageAnalysis(
                type = MessageType.Sms,
                messageState = state.value,
                attachedFiles = null,
                onSuccess = onAnalysisStart,
                onError = errorHandler::handle
            )
        },
        screenTitle = stringResource(R.string.sms_message_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = submitEnabled,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an SMS from the device's inbox.
        MessageSection(
            state = state,
            onSenderChange = { state.value = state.value.copy(sender = it) },
            onContentChange = { state.value = state.value.copy(content = it) },
            title = stringResource(R.string.sms_message_tool_section_title),
            description = stringResource(R.string.sms_message_tool_section_description),
            isEmail = false
        )
    }
}