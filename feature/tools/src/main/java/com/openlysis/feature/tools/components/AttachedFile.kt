package com.openlysis.feature.tools.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.TextInput
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

/**
 * Displays an attached file with filename, detach button, and password input with visibility toggle.
 *
 * @param onDetachClick Callback when the detach button is clicked.
 * @param onPasswordChange Callback when the password input changes.
 * @param passwordValue The value of the password to display on the UI.
 * @param filename The name of the attached file.
 * @param modifier Modifier for styling.
 */
@Composable
internal fun AttachedFile(
    onDetachClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    passwordValue: String?,
    filename: String,
    modifier: Modifier = Modifier
) {
    val passwordVisualTransformation = PasswordVisualTransformation()
    val noneVisualTransformation = VisualTransformation.None
    var inputVisualTransformation by remember {
        mutableStateOf<VisualTransformation>(passwordVisualTransformation)
    }
    var showPassword by remember { mutableStateOf(true) }

    val togglePasswordTransformationIcon = if (showPassword) AppIcons.Eye else AppIcons.EyeOff
    val togglePasswordTransformationIconAlt =
        if (showPassword) {
            stringResource(R.string.attached_file_show_password_button_icon_alt)
        } else {
            stringResource(R.string.attached_file_hide_password_button_icon_alt)
        }

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value300,
                alignment = Alignment.CenterVertically
            ),
        modifier =
            modifier
                .clip(RoundedCornerShape(LocalAppRadius.current.value100))
                .background(LocalAppColorScheme.current.background.default.secondary)
                .padding(all = LocalAppSpacing.current.value300)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = AppIcons.File,
                contentDescription = stringResource(R.string.attached_file_header_icon_alt),
                tint = LocalAppColorScheme.current.icon.default.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = filename,
                style = LocalAppTypography.current.bodyBase,
                color = LocalAppColorScheme.current.text.default.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            AppButton(
                type = ButtonType.Danger,
                size = SizeType.ExtraSmall,
                onClick = onDetachClick,
                displayLabel = false,
                displayIcon = true,
                icon = AppIcons.Cross,
                iconAlt = stringResource(R.string.attached_file_detach_button_icon_alt)
            )
        }
        TextInput(
            value = passwordValue ?: "",
            onValueChange = { onPasswordChange(it) },
            label = stringResource(R.string.attached_file_password_input_label),
            trailingButton = {
                AppButton(
                    type = ButtonType.Tertiary,
                    size = SizeType.Small,
                    onClick = {
                        inputVisualTransformation =
                            if (showPassword) {
                                noneVisualTransformation
                            } else {
                                passwordVisualTransformation
                            }
                        showPassword = !showPassword
                    },
                    displayLabel = false,
                    displayIcon = true,
                    icon = togglePasswordTransformationIcon,
                    iconAlt = togglePasswordTransformationIconAlt
                )
            },
            visualTransformation = inputVisualTransformation
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AttachedFilePreview() {
    OpenlysisTheme(darkTheme = false) {
        AttachedFile(
            onDetachClick = { },
            onPasswordChange = { },
            passwordValue = "mySuperSecretPassword1234",
            filename = "test.pdf"
        )
    }
}