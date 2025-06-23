package com.openlysis.feature.tools.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.tools.R

/**
 * Section for entering message details in a modal, including sender, subject (if email), and content fields.
 *
 * @param onSenderChange Callback when the sender input changes.
 * @param onContentChange Callback when the content input changes.
 * @param title The title of the section.
 * @param description The description of the section.
 * @param isEmail Whether the message is an email (shows subject field if true).
 * @param modifier Modifier for styling.
 * @param onSubjectChange Optional callback when the subject input changes.
 */
@Composable
internal fun MessageModalSection(
    onSenderChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    title: String,
    description: String,
    isEmail: Boolean,
    modifier: Modifier = Modifier,
    onSubjectChange: ((String) -> Unit)? = null
) {
    var senderValue by rememberSaveable { mutableStateOf("") }
    var contentValue by rememberSaveable { mutableStateOf("") }
    val senderKeyboardOptions =
        remember {
            KeyboardOptions(keyboardType = if (isEmail) KeyboardType.Email else KeyboardType.Text)
        }
    val textKeyboardOptions =
        remember {
            KeyboardOptions(keyboardType = KeyboardType.Text)
        }

    ToolModalSection(
        title = title,
        description = description,
        modifier = modifier
    ) {
        TextInput(
            senderValue,
            onValueChange = { nv ->
                senderValue = nv
                onSenderChange(nv)
            },
            label = stringResource(R.string.analyze_message_sender_input_label),
            keyboardOptions = senderKeyboardOptions
        )

        if (isEmail) {
            var subjectValue by rememberSaveable { mutableStateOf("") }
            TextInput(
                subjectValue,
                onValueChange = { nv ->
                    subjectValue = nv
                    onSubjectChange?.invoke(nv)
                },
                label = stringResource(R.string.analyze_message_subject_input_label),
                keyboardOptions = textKeyboardOptions
            )
        }

        TextInput(
            contentValue,
            onValueChange = { nv ->
                contentValue = nv
                onContentChange(nv)
            },
            label = stringResource(R.string.analyze_message_content_input_label),
            keyboardOptions = textKeyboardOptions,
            singleLine = false
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MessageModalSectionPreview() {
    OpenlysisTheme(darkTheme = false) {
        MessageModalSection(
            onSenderChange = { },
            onContentChange = { },
            title = "Test Title",
            description = "This is a description.",
            isEmail = true
        )
    }
}