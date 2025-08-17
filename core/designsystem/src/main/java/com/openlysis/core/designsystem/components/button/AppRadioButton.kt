package com.openlysis.core.designsystem.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A custom radio button component with a label.
 *
 * @param onClick Callback invoked when the radio button is toggled, passing the new selected state.
 * @param selected Whether the radio button is currently selected.
 * @param label The text label displayed next to the radio button.
 * @param modifier Modifier to be applied to the component.
 */
@Composable
fun AppRadioButton(
    onClick: (Boolean) -> Unit,
    selected: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
        modifier =
            modifier
                .heightIn(min = 48.dp)
                .toggleable(
                    value = selected,
                    onValueChange = { onClick(it) },
                    role = Role.RadioButton
                )
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors =
                RadioButtonColors(
                    selectedColor = LocalAppColorScheme.current.background.brand.primary,
                    unselectedColor = LocalAppColorScheme.current.border.neutral.primary,
                    disabledSelectedColor = LocalAppColorScheme.current.background.disabled.primary,
                    disabledUnselectedColor = LocalAppColorScheme.current.border.disabled.primary
                )
        )

        Text(
            text = label,
            style = LocalAppTypography.current.bodyBase,
            color = LocalAppColorScheme.current.text.default.primary,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(end = LocalAppSpacing.current.value300)
        )
    }
}

@PreviewLightDark
@Composable
private fun AppRadioButtonPreview() {
    var selected by remember { mutableStateOf(false) }

    OpenlysisTheme {
        AppRadioButton(
            onClick = { selected = !selected },
            selected = selected,
            label = "I'm a radio button"
        )
    }
}