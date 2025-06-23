package com.openlysis.feature.tools.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.alert.Alert
import com.openlysis.core.designsystem.components.alert.AlertType
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.tools.R

/**
 * Section for attaching files in a tool modal, including an alert and attach button.
 *
 * @param title The title of the section.
 * @param description The description of the section.
 * @param modifier Modifier for styling.
 */
@Composable
internal fun AttachFilesModalSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    // TODO: Add functionality to attach files.
    // TODO: Use settings to configure the max amount of files that can be attached.
    // TODO: Use settings to configure the max size of a file that can be attached.
    // TODO: If max amount of attached files is reached, disable attach button.
    // TODO: If a file is too large, display error.
    ToolModalSection(
        title = title,
        description = description,
        modifier = modifier
    ) {
        Alert(
            type = AlertType.Warning,
            text = stringResource(R.string.attach_file_warning_alert)
        )

        AppButton(
            type = ButtonType.Positive,
            size = SizeType.Default,
            onClick = { },
            displayLabel = true,
            label = stringResource(R.string.attach_file_button_label),
            displayIcon = true,
            icon = AppIcons.Plus,
            iconAlt = stringResource(R.string.attach_file_button_icon_alt)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AttachFilesModalSectionPreview() {
    OpenlysisTheme(darkTheme = false) {
        AttachFilesModalSection(
            title = "Test title",
            description = "This is a description"
        )
    }
}