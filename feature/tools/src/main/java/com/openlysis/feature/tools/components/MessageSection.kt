package com.openlysis.feature.tools.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.tools.R

/**
 * Section for entering message details for a message analysis tool. Includes sender, subject (if email), and content.
 *
 * @param state State holder for the message fields
 * @param onSenderChange Callback for when the sender field changes
 * @param onContentChange Callback for when the content field changes
 * @param title The title of the section
 * @param description The description of the section
 * @param isEmail Whether the message is an email (shows subject field if true)
 * @param modifier Modifier for styling
 * @param onSubjectChange Callback for when the subject field changes (only used if isEmail is true)
 */
@Composable
internal fun MessageSection(
    state: State<MessageState>,
    onSenderChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    title: String,
    description: String,
    isEmail: Boolean,
    modifier: Modifier = Modifier,
    onSubjectChange: ((String) -> Unit)? = null
) {
    val stateValue by state
    val senderKeyboardOptions =
        remember {
            KeyboardOptions(
                keyboardType = if (isEmail) KeyboardType.Email else KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        }
    val textKeyboardOptions =
        remember {
            KeyboardOptions(keyboardType = KeyboardType.Text)
        }

    ToolSection(
        title = title,
        description = description,
        modifier = modifier
    ) {
        TextInput(
            value = stateValue.sender,
            onValueChange = onSenderChange,
            label = stringResource(R.string.analyze_message_sender_input_label),
            keyboardOptions = senderKeyboardOptions
        )

        if (isEmail) {
            TextInput(
                value = stateValue.subject ?: "",
                onValueChange = onSubjectChange ?: { },
                label = stringResource(R.string.analyze_message_subject_input_label),
                keyboardOptions = textKeyboardOptions.copy(imeAction = ImeAction.Next)
            )
        }

        TextInput(
            value = stateValue.content,
            onValueChange = onContentChange,
            label = stringResource(R.string.analyze_message_content_input_label),
            keyboardOptions = textKeyboardOptions,
            singleLine = false
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MessageSectionPreview() {
    OpenlysisTheme(darkTheme = false) {
        MessageSection(
            state = remember { mutableStateOf(MessageState()) },
            onSenderChange = { },
            onContentChange = { },
            title = "Test Title",
            description = "This is a description.",
            isEmail = true
        )
    }
}