package com.openlysis.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A checkbox component with a text label.
 *
 * @param onCheckedChange Callback invoked when the checkbox state changes
 * @param checked The current state of the checkbox
 * @param label The text label displayed next to the checkbox
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
fun AppCheckbox(
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean,
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
                    value = checked,
                    onValueChange = { onCheckedChange(it) },
                    role = Role.Checkbox
                )
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors =
                CheckboxColors(
                    checkedCheckmarkColor = LocalAppColorScheme.current.icon.brand.onPrimary,
                    uncheckedCheckmarkColor = Color.Transparent,
                    checkedBoxColor = LocalAppColorScheme.current.background.brand.primary,
                    uncheckedBoxColor = Color.Transparent,
                    disabledCheckedBoxColor =
                        LocalAppColorScheme.current.background.disabled.primary,
                    disabledUncheckedBoxColor = Color.Transparent,
                    checkedBorderColor = LocalAppColorScheme.current.background.brand.primary,
                    uncheckedBorderColor = LocalAppColorScheme.current.border.neutral.primary,
                    disabledBorderColor = Color.Transparent,
                    disabledUncheckedBorderColor =
                        LocalAppColorScheme.current.border.disabled.primary,
                    disabledIndeterminateBoxColor = Color.Transparent,
                    disabledIndeterminateBorderColor =
                        LocalAppColorScheme.current.border.disabled.primary
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
private fun AppCheckboxPreview() {
    var checked by remember { mutableStateOf(false) }
    OpenlysisTheme {
        AppCheckbox(
            onCheckedChange = { checked = !checked },
            checked = checked,
            label = "I'm a checkbox"
        )
    }
}