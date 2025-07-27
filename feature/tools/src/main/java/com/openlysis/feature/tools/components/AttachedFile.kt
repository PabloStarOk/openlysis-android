package com.openlysis.feature.tools.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.tools.R
import com.openlysis.feature.tools.model.AttachedFileError

/**
 * Displays an attached file with filename, detach button, and password input with visibility toggle.
 *
 * @param onSetPasswordRequest Callback when the user requests to set the password for the file.
 * @param onDetachClick Callback when the detach button is clicked.
 * @param filename The name of the attached file.
 * @param password The value of the password to display on the UI.
 * @param attachedFileError The error state for the attached file, if any.
 * @param modifier Modifier for styling.
 */
@Composable
internal fun AttachedFile(
    onSetPasswordRequest: () -> Unit,
    onDetachClick: () -> Unit,
    filename: String,
    password: String,
    attachedFileError: AttachedFileError?,
    modifier: Modifier = Modifier
) {
    val isInvalid = attachedFileError != null

    Box(
        modifier =
            modifier.border(
                width = 1.dp,
                color = LocalAppColorScheme.current.border.default.primary,
                shape = RoundedCornerShape(LocalAppRadius.current.value100)
            )
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(
                    space = LocalAppSpacing.current.value300,
                    alignment = Alignment.CenterVertically
                ),
            modifier = Modifier.padding(all = LocalAppSpacing.current.value400)
        ) {
            Header(
                onDetachClick = onDetachClick,
                filename = filename,
                isInvalid = isInvalid
            )

            if (isInvalid) {
                ErrorMessage(error = attachedFileError)
            } else {
                PasswordSection(
                    onConfigurePasswordClick = onSetPasswordRequest,
                    password = password
                )
            }
        }
    }
}

@Composable
private fun Header(
    onDetachClick: () -> Unit,
    filename: String,
    isInvalid: Boolean
) {
    val headerIcon = if (isInvalid) AppIcons.OctagonAlert else AppIcons.File
    val headerIconAlt =
        if (isInvalid) {
            R.string.attached_file_header_error_icon_alt
        } else {
            R.string.attached_file_header_icon_alt
        }
    val headerIconColor =
        if (isInvalid) {
            LocalAppColorScheme.current.icon.danger.secondary
        } else {
            LocalAppColorScheme.current.icon.default.primary
        }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = headerIcon,
            contentDescription = stringResource(headerIconAlt),
            tint = headerIconColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = filename,
            style = LocalAppTypography.current.bodySmall,
            color = LocalAppColorScheme.current.text.default.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        AppButton(
            type = ButtonType.Tertiary,
            size = SizeType.ExtraSmall,
            onClick = onDetachClick,
            displayLabel = false,
            displayIcon = true,
            icon = AppIcons.Cross,
            iconAlt = stringResource(R.string.attached_file_detach_button_icon_alt)
        )
    }
}

@Composable
private fun ErrorMessage(error: AttachedFileError) {
    val errorMessageResId = errorMessageResourceIds.getValue(error)
    Text(
        text = stringResource(errorMessageResId),
        style = LocalAppTypography.current.bodySmall,
        color = LocalAppColorScheme.current.text.danger.secondary
    )
}

@Composable
private fun PasswordSection(
    onConfigurePasswordClick: () -> Unit,
    password: String?
) {
    val passwordExists = password != null && password.isNotEmpty()
    if (passwordExists) {
        MaskedPassword(
            onConfigurePasswordClick = onConfigurePasswordClick,
            password = password
        )
    } else {
        AppButton(
            type = ButtonType.Tertiary,
            size = SizeType.ExtraSmall,
            onClick = onConfigurePasswordClick,
            displayLabel = true,
            displayIcon = true,
            label = stringResource(R.string.attached_file_add_password_button_label),
            icon = AppIcons.Key,
            iconAlt = stringResource(R.string.attached_file_add_password_button_icon_alt)
        )
    }
}

@Composable
private fun MaskedPassword(
    onConfigurePasswordClick: () -> Unit,
    password: String
) {
    val visualTransformation = PasswordVisualTransformation()
    val transformedPassword = visualTransformation.filter(AnnotatedString(password))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value100)
    ) {
        AppButton(
            type = ButtonType.Tertiary,
            size = SizeType.Small,
            onClick = onConfigurePasswordClick,
            displayLabel = false,
            displayIcon = true,
            icon = AppIcons.Edit,
            iconAlt = stringResource(R.string.attached_file_edit_password_button_icon_alt)
        )

        Column {
            Text(
                text = stringResource(R.string.attached_file_password_label),
                style = LocalAppTypography.current.bodyXSmall,
                color = LocalAppColorScheme.current.text.default.secondary
            )
            Text(
                text = transformedPassword.text,
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.default.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private val errorMessageResourceIds =
    mapOf(
        Pair(
            AttachedFileError.NoData,
            R.string.attached_file_error_no_data
        ),
        Pair(
            AttachedFileError.TooLarge,
            R.string.attached_file_error_too_large
        ),
        Pair(
            AttachedFileError.AlreadyAttached,
            R.string.attached_file_error_already_attached
        ),
        Pair(
            AttachedFileError.LimitReached,
            R.string.attached_file_error_limit_reached
        )
    )

@Preview(showSystemUi = false)
@Composable
private fun AttachedFilePreview() {
    OpenlysisTheme(darkTheme = true) {
        AttachedFile(
            onSetPasswordRequest = { },
            onDetachClick = { },
            password = "myPassword",
            filename = "test.pdf",
            attachedFileError = AttachedFileError.NoData
        )
    }
}