package com.openlysis.feature.tools

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.tools.components.AnalyzeEmailModalState
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.MessageSection
import com.openlysis.feature.tools.components.MessageSectionState
import com.openlysis.feature.tools.components.ToolModal
import com.openlysis.feature.tools.components.rememberAnalyzeEmailModalState
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings

/**
 * A modal for analyzing an email, allowing the user to input sender, subject, content, and attach files.
 *
 * @param onSubmitClick Called when the submit button is clicked, with the current message state and attached files.
 * @param onDismissRequest Called when the modal should be dismissed.
 * @param toolState The state holder for the email analysis modal, containing message and file attachment states.
 * @param modalState The state of the bottom sheet modal, controlling its appearance and behavior.
 * @param messageUiNotifier Notifier for displaying UI messages and handling user feedback.
 * @param modifier Modifier for styling the modal's layout and appearance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalyzeEmailModal(
    onSubmitClick: (MessageSectionState, List<AttachedFileData>) -> Unit,
    onDismissRequest: () -> Unit,
    toolState: AnalyzeEmailModalState,
    modalState: SheetState,
    messageUiNotifier: MessageUiNotifier,
    modifier: Modifier = Modifier
) {
    val submitEnabled by
        remember(toolState.messageState) {
            derivedStateOf {
                toolState.messageState.value.sender
                    .isNotEmpty() &&
                    toolState.messageState.value.content
                        .isNotEmpty()
            }
        }
    val attachFilesEnabled by
        remember(toolState.attachedFiles, toolState.fileAttachmentSettings.maxFilesAmount) {
            derivedStateOf {
                toolState.attachedFiles.size < toolState.fileAttachmentSettings.maxFilesAmount
            }
        }

    ToolModal(
        onSubmitClick = {
            onSubmitClick(toolState.messageState.value, toolState.attachedFiles)
        },
        onDismissRequest = onDismissRequest,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = submitEnabled,
        state = modalState,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an email from the inbox instead of filling information manually.
        MessageSection(
            state = toolState.messageState,
            title = stringResource(R.string.email_message_tool_modal_title),
            description = stringResource(R.string.email_message_tool_modal_description),
            isEmail = true
        )

        AttachFilesSection(
            attachedFiles = toolState.attachedFiles,
            onFileAttach = { toolState.addAttachedFile(it) },
            onFileDetach = { toolState.removeAttachedFile(it) },
            onFilePasswordChange = { file, password ->
                toolState.updateAttachedFilePassword(file, password)
            },
            enabled = attachFilesEnabled,
            title = stringResource(R.string.email_message_tool_modal_file_title),
            description = stringResource(R.string.email_message_tool_modal_file_description),
            settings = toolState.fileAttachmentSettings
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = false)
@Composable
private fun DialogPreview() {
    val context = LocalContext.current
    val messageUiNotifier = MessageUiNotifier(context)

    OpenlysisTheme(darkTheme = false) {
        AnalyzeEmailModal(
            onSubmitClick = { _, _ -> },
            onDismissRequest = { },
            toolState =
                rememberAnalyzeEmailModalState(
                    messageUiNotifier = messageUiNotifier,
                    fileAttachmentSettings =
                        FileAttachmentSettings(
                            maxFilesAmount = 0,
                            maxFileSize = 0
                        )
                ),
            modalState =
                SheetState(
                    skipPartiallyExpanded = true,
                    initialValue = SheetValue.Expanded,
                    density = Density(LocalDensity.current.density)
                ),
            messageUiNotifier = messageUiNotifier
        )
    }
}