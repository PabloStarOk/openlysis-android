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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.MessageSection
import com.openlysis.feature.tools.components.MessageState
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.components.rememberAttachFilesState
import com.openlysis.feature.tools.data.AnalysisRequestState

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
    val messageState = rememberSaveable { mutableStateOf(MessageState()) }
    val attachFilesState =
        rememberAttachFilesState(
            settings = viewModel.fileAttachmentSettings,
            messageUiNotifier = messageUiNotifier
        )
    val submitEnabled by
        remember(messageState) {
            derivedStateOf {
                messageState.value.submitEnabled
            }
        }
    val attachFilesEnabled by
        remember(attachFilesState) {
            derivedStateOf {
                attachFilesState.attachmentEnabled
            }
        }
    val analysisRequestState = viewModel.currentAnalysisRequest.collectAsStateWithLifecycle()

    ToolScreenScaffold(
        onSubmitRequest = {
            viewModel.startMessageAnalysis(
                type = MessageType.Email,
                messageState = messageState.value,
                attachedFiles = attachFilesState.attachedFiles
            )
        },
        onGoToAnalysisRequest = {
            if (analysisRequestState.value is AnalysisRequestState.Success.Message) {
                val value = analysisRequestState.value as AnalysisRequestState.Success.Message
                onAnalysisStart(value.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelCurrentRequest,
        analysisRequestState = analysisRequestState,
        screenTitle = stringResource(R.string.email_message_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = submitEnabled,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an email from the inbox instead of filling information manually.
        MessageSection(
            state = messageState,
            onSenderChange = { messageState.value = messageState.value.copy(sender = it) },
            onSubjectChange = { messageState.value = messageState.value.copy(subject = it) },
            onContentChange = { messageState.value = messageState.value.copy(content = it) },
            title = stringResource(R.string.email_message_tool_section_title),
            description = stringResource(R.string.email_message_tool_section_description),
            isEmail = true
        )

        AttachFilesSection(
            attachedFiles = attachFilesState.attachedFiles,
            onFileAttach = attachFilesState::attachFile,
            onFileDetach = attachFilesState::detachFile,
            onFilePasswordChange = attachFilesState::updateFilePassword,
            enabled = attachFilesEnabled,
            title = stringResource(R.string.email_message_tool_file_section_title),
            description = stringResource(R.string.email_message_tool_file_section_description),
            settings = viewModel.fileAttachmentSettings
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