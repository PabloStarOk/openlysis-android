package com.openlysis.feature.tools.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.tools.R

/**
 * Section for entering message details for a message analysis tool. Includes sender, subject (if email), and content.
 *
 * @param state State holder for the message fields
 * @param title The title of the section.
 * @param description The description of the section.
 * @param isEmail Whether the message is an email (shows subject field if true).
 * @param modifier Modifier for styling.
 */
@Composable
internal fun MessageSection(
    state: MutableState<MessageSectionState>,
    title: String,
    description: String,
    isEmail: Boolean,
    modifier: Modifier = Modifier
) {
    var stateValue by state
    val senderKeyboardOptions =
        remember {
            KeyboardOptions(keyboardType = if (isEmail) KeyboardType.Email else KeyboardType.Text)
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
            stateValue.sender,
            onValueChange = { stateValue = stateValue.copy(sender = it) },
            label = stringResource(R.string.analyze_message_sender_input_label),
            keyboardOptions = senderKeyboardOptions
        )

        if (isEmail) {
            TextInput(
                stateValue.subject ?: "",
                onValueChange = { stateValue = stateValue.copy(subject = it) },
                label = stringResource(R.string.analyze_message_subject_input_label),
                keyboardOptions = textKeyboardOptions
            )
        }

        TextInput(
            stateValue.content,
            onValueChange = { stateValue = stateValue.copy(content = it) },
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
            state = remember { mutableStateOf(MessageSectionState()) },
            title = "Test Title",
            description = "This is a description.",
            isEmail = true
        )
    }
}