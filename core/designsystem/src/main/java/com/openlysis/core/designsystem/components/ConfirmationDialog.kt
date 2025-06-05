package com.openlysis.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.openlysis.core.designsystem.R
import com.openlysis.core.designsystem.components.alert.Alert
import com.openlysis.core.designsystem.components.alert.AlertType
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A confirmation dialog.
 *
 * @param title The title of the dialog.
 * @param description The description of the dialog.
 * @param onConfirm A callback function that is invoked when the user confirms the dialog.
 * @param onCancel A callback function that is invoked when the user cancels the dialog.
 * @param modifier A [Modifier] for this composable.
 * @param additionalContent A composable function that provides additional content to be displayed in the dialog.
 * @param displayAlert A boolean indicating whether to display an alert in the dialog.
 * @param alertType The type of alert to display.
 * @param alertText The text to display in the alert.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    additionalContent: @Composable () -> Unit = { },
    displayAlert: Boolean = false,
    alertType: AlertType = AlertType.Warning,
    alertText: String = ""
) {
    Dialog(
        onDismissRequest = onCancel
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
            modifier =
                modifier
                    .clip(RoundedCornerShape(LocalAppRadius.current.value100))
                    .background(LocalAppColorScheme.current.background.default.primary)
                    .border(
                        width = 1.dp,
                        color = LocalAppColorScheme.current.border.default.primary
                    ).padding(all = LocalAppSpacing.current.value600)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = LocalAppTypography.current.title5,
                    color = LocalAppColorScheme.current.text.brand.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = description,
                    style = LocalAppTypography.current.bodyBase,
                    color = LocalAppColorScheme.current.text.default.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            additionalContent()

            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400)
            ) {
                if (displayAlert && alertText.isNotBlank()) {
                    Alert(
                        type = alertType,
                        text = alertText,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppButton(
                        type = ButtonType.Secondary,
                        size = SizeType.Default,
                        onClick = onConfirm,
                        displayLabel = true,
                        displayIcon = true,
                        label = stringResource(R.string.confirmation_dialog_accept),
                        icon = ImageVector.vectorResource(R.drawable.check_icon),
                        iconAlt = stringResource(R.string.confirmation_dialog_accept_icon_alt),
                        modifier = Modifier.weight(1f)
                    )

                    AppButton(
                        type = ButtonType.Secondary,
                        size = SizeType.Default,
                        onClick = onCancel,
                        displayLabel = true,
                        displayIcon = true,
                        label = stringResource(R.string.confirmation_dialog_cancel),
                        icon = ImageVector.vectorResource(R.drawable.x_icon),
                        iconAlt = stringResource(R.string.confirmation_dialog_cancel_icon_alt),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ConfirmationDialogPreview() {
    OpenlysisTheme(darkTheme = false) {
        ConfirmationDialog(
            title = "Example",
            description = "This is an example.",
            onConfirm = { },
            onCancel = { },
            additionalContent = { Text(text = "This is additional data.") },
            displayAlert = true,
            alertType = AlertType.Warning,
            alertText = "This is a confirmation dialog."
        )
    }
}