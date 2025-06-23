package com.openlysis.feature.tools

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.tools.components.AttachFilesModalSection
import com.openlysis.feature.tools.components.MessageModalSection
import com.openlysis.feature.tools.components.MessageModalSectionState
import com.openlysis.feature.tools.components.ToolModal

/**
 * A modal for analyzing an email, allowing the user to input sender, subject, content, and attach files.
 *
 * @param onDismissRequest Called when the modal should be dismissed.
 * @param modalState The state of the bottom sheet modal.
 * @param modifier Modifier for styling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalyzeEmailModal(
    onDismissRequest: () -> Unit,
    modalState: SheetState,
    modifier: Modifier = Modifier
) {
    val messageState = rememberSaveable { mutableStateOf(MessageModalSectionState()) }
    val submitEnabled by
        remember(messageState.value) {
            derivedStateOf {
                messageState.value.sender.isNotEmpty() && messageState.value.content.isNotEmpty()
            }
        }

    ToolModal(
        onSubmitClick = { }, // TODO: Add submit functionality.
        onDismissRequest = onDismissRequest,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        submitEnabled = submitEnabled,
        state = modalState,
        modifier = modifier
    ) {
        // TODO: Add functionality to select an email from the inbox instead of filling information manually.
        MessageModalSection(
            state = messageState,
            title = stringResource(R.string.email_message_tool_modal_title),
            description = stringResource(R.string.email_message_tool_modal_description),
            isEmail = true
        )
        AttachFilesModalSection(
            title = stringResource(R.string.email_message_tool_modal_file_title),
            description = stringResource(R.string.email_message_tool_modal_file_description)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = false)
@Composable
private fun DialogPreview() {
    OpenlysisTheme(darkTheme = false) {
        AnalyzeEmailModal(
            onDismissRequest = { },
            modalState =
                SheetState(
                    skipPartiallyExpanded = true,
                    initialValue = SheetValue.Expanded,
                    density = Density(LocalDensity.current.density)
                )
        )
    }
}