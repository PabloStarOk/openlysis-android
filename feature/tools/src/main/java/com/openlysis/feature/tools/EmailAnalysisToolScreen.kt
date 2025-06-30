package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.MessageSection
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.components.rememberEmailAnalysisState

/**
 * Composable screen for email analysis tool that allows users to input email details and attachments.
 *
 * @param viewModel The view model handling the business logic for the tools screen
 * @param onAnalysisStart Callback invoked when email analysis starts, provides the initial analysis result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Modifier for customizing the layout. Defaults to [Modifier].
 */
@Composable
internal fun EmailAnalysisToolScreen(
    viewModel: ToolsScreenViewModel,
    onAnalysisStart: (MessageAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messageUiNotifier = remember { MessageUiNotifier(context) }
    val errorHandler = remember { AnalysisErrorUiHandler(messageUiNotifier) }
    val state =
        rememberEmailAnalysisState(
            messageUiNotifier = messageUiNotifier,
            fileAttachmentSettings = viewModel.fileAttachmentSettings
        )
    val submitEnabled by
        remember(state.messageState) {
            derivedStateOf {
                state.messageState.value.submitEnabled
            }
        }
    val attachFilesEnabled by
        remember(state.attachedFiles, state.fileAttachmentSettings.maxFilesAmount) {
            derivedStateOf {
                state.attachedFiles.size < state.fileAttachmentSettings.maxFilesAmount
            }
        }

    ToolScreenScaffold(
        onSubmitClick = {
            viewModel.startMessageAnalysis(
                type = MessageType.Email,
                messageState = state.messageState.value,
                attachedFiles = state.attachedFiles,
                onSuccess = onAnalysisStart,
                onError = errorHandler::handle
            )
        },
        screenTitle = stringResource(R.string.email_message_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = submitEnabled,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an email from the inbox instead of filling information manually.
        MessageSection(
            state = state.messageState,
            onSenderChange = state::updateSender,
            onSubjectChange = state::updateSubject,
            onContentChange = state::updateContent,
            title = stringResource(R.string.email_message_tool_section_title),
            description = stringResource(R.string.email_message_tool_section_description),
            isEmail = true
        )

        AttachFilesSection(
            attachedFiles = state.attachedFiles,
            onFileAttach = state::addAttachedFile,
            onFileDetach = state::removeAttachedFile,
            onFilePasswordChange = state::updateAttachedFilePassword,
            enabled = attachFilesEnabled,
            title = stringResource(R.string.email_message_tool_file_section_title),
            description = stringResource(R.string.email_message_tool_file_section_description),
            settings = state.fileAttachmentSettings
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AnalyzeEmailScreenPreview() {
    OpenlysisTheme(darkTheme = false) {
        EmailAnalysisToolScreen(
            onAnalysisStart = { },
            onTopBarUpdate = { },
            viewModel = hiltViewModel()
        )
    }
}