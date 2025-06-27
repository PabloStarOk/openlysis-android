package com.openlysis.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.color.AppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A composable function that displays a text input field with a label and optional error message.
 *
 * @param value The current value of the text input field.
 * @param onValueChange A callback function that is invoked when the value of the text input field changes.
 * @param label The label to display above the text input field.
 * @param modifier A [Modifier] to apply to the text input field.
 * @param isError A boolean value that indicates whether the text input field is in an error state.
 * @param enabled A boolean value that indicates whether the text input field is enabled.
 * @param trailingButton A composable function that displays a trailing button in the text input field.
 * @param supportingText A composable function that displays supporting text below the text input field.
 * @param visualTransformation A [VisualTransformation] to apply to the text input field.
 * @param keyboardOptions A [KeyboardOptions] to apply to the text input field.
 * @param keyboardActions A [KeyboardActions] to apply to the text input field.
 * @param singleLine A boolean value that indicates whether the text input field should be single-line.
 */
@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    trailingButton: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true
) {
    val appColorScheme = LocalAppColorScheme.current
    val appRadius = LocalAppRadius.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val defaultColors = TextFieldDefaults.colors()
    val colors =
        remember(defaultColors, appColorScheme) {
            getTextInputColors(defaultColors, appColorScheme)
        }
    val borderColor by animateColorAsState(
        when {
            isError -> appColorScheme.border.danger.secondary
            isFocused -> appColorScheme.border.brand.primary
            !enabled -> appColorScheme.border.disabled.primary
            else -> appColorScheme.border.default.primary
        }
    )

    val textFieldModifier =
        remember(appRadius, borderColor) {
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(appRadius.value100))
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(appRadius.value100)
                )
        }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.animateContentSize()
    ) {
        TextField(
            colors = colors,
            textStyle = LocalAppTypography.current.bodyBase,
            shape = RoundedCornerShape(LocalAppRadius.current.value100),
            modifier = textFieldModifier,
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            isError = isError,
            enabled = enabled,
            trailingIcon = trailingButton,
            interactionSource = interactionSource
        )

        if (supportingText != null) {
            supportingText()
        }
    }
}

/**
 * Creates a custom [TextFieldColors] configuration based on the provided [TextFieldColors]
 * and [AppColorScheme].
 */
private fun getTextInputColors(
    defaultColors: TextFieldColors,
    appColorScheme: AppColorScheme
): TextFieldColors =
    defaultColors.copy(
        cursorColor = appColorScheme.background.brand.primary,
        focusedTextColor = appColorScheme.text.default.primary,
        focusedLabelColor = appColorScheme.text.brand.primary,
        focusedContainerColor = appColorScheme.background.default.primary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedTextColor = appColorScheme.text.default.primary,
        unfocusedLabelColor = appColorScheme.text.default.secondary,
        unfocusedContainerColor = appColorScheme.background.default.primary,
        unfocusedIndicatorColor = Color.Transparent,
        errorTextColor = appColorScheme.text.default.primary,
        errorLabelColor = appColorScheme.text.danger.secondary,
        errorContainerColor = appColorScheme.background.default.primary,
        errorIndicatorColor = Color.Transparent,
        disabledTextColor = appColorScheme.text.disabled.onPrimary,
        disabledLabelColor = appColorScheme.text.disabled.onPrimary,
        disabledContainerColor = appColorScheme.background.disabled.primary,
        textSelectionColors =
            TextSelectionColors(
                appColorScheme.background.brand.primary,
                appColorScheme.background.brand.secondary
            )
    )

@Preview(showSystemUi = false)
@Composable
private fun TextInputPreview() {
    OpenlysisTheme(darkTheme = false) {
        TextInput(
            value = "Input",
            onValueChange = { },
            label = "Label",
            isError = false
        )
    }
}