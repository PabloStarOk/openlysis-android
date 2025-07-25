package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.MessageSection
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.data.AnalysisRequestState
import com.openlysis.feature.tools.util.showAttachmentErrorUiMessage

/**
 * Composable screen for email analysis tool that allows users to input email details and attachments.
 *
 * @param viewModel The view model handling the business logic for the email analysis tool screen
 * @param onAnalysisStart Callback invoked when email analysis starts, provides the initial analysis result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Modifier for customizing the layout. Defaults to [Modifier].
 */
@Composable
internal fun EmailAnalysisToolScreen(
    viewModel: EmailAnalysisToolScreenViewModel,
    onAnalysisStart: (MessageAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messageUiNotifier = remember { MessageUiNotifier(context) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val requestState = uiState.requestState
    if (uiState.invalidAttachedFiles.isNotEmpty()) {
        uiState.invalidAttachedFiles.forEach {
            if (it.error == null) {
                return@forEach
            }
            showAttachmentErrorUiMessage(
                context = context,
                messageUiNotifier = messageUiNotifier,
                error = it.error,
                attachmentSettings = viewModel.attachmentSettings
            )
        }

        viewModel.clearInvalidAttachedFiles()
    }

    ToolScreenScaffold(
        onSubmitRequest = viewModel::startAnalysis,
        onGoToAnalysisRequest = {
            if (requestState is AnalysisRequestState.Success.Message) {
                onAnalysisStart(requestState.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelRequest,
        analysisRequestState = requestState,
        screenTitle = stringResource(R.string.email_message_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = uiState.canRequestAnalysis,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an email from the inbox instead of filling information manually.
        MessageSection(
            state = uiState.message,
            onSenderChange = viewModel::updateSender,
            onSubjectChange = viewModel::updateSubject,
            onContentChange = viewModel::updateContent,
            title = stringResource(R.string.email_message_tool_section_title),
            description = stringResource(R.string.email_message_tool_section_description),
            isEmail = true
        )

        AttachFilesSection(
            attachedFiles = uiState.attachedFiles.values.toList(),
            onFileAttach = viewModel::attachFile,
            onFileDetach = viewModel::detachFile,
            onFilePasswordChange = viewModel::updateAttachedFilePassword,
            enabled = uiState.canAttachFiles,
            title = stringResource(R.string.email_message_tool_file_section_title),
            description = stringResource(R.string.email_message_tool_file_section_description),
            settings = viewModel.attachmentSettings
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