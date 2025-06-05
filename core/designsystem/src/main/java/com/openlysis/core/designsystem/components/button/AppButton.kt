package com.openlysis.core.designsystem.components.button

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A composable function that displays a customizable button with optional icon and label.
 *
 * @param buttonType The type of button, which determines its appearance (e.g., primary, secondary).
 * @param onClick The callback function to be executed when the button is clicked.
 * @param displayLabel A boolean indicating whether to display the label.
 * @param displayIcon A boolean indicating whether to display the icon.
 * @param modifier Optional [Modifier] for customizing the button's layout and appearance.
 * @param label The text to be displayed as the button's label. Defaults to an empty string.
 * @param icon The [ImageVector] to be displayed as the button's icon. Defaults to null.
 * @param iconAlt The content description for the icon, used for accessibility. Defaults to null.
 */
@Composable
fun AppButton(
    buttonType: ButtonType,
    onClick: () -> Unit,
    displayLabel: Boolean,
    displayIcon: Boolean,
    modifier: Modifier = Modifier,
    label: String? = "",
    icon: ImageVector? = null,
    iconAlt: String? = null
) {
    val buttonData = ButtonTypeColorsMap.getValue(buttonType)
    val ripple =
        ripple(
            bounded = true,
            color = buttonData.getRippleColor(LocalAppColorScheme.current)
        )

    Surface(
        color = buttonData.getBackgroundColor(LocalAppColorScheme.current),
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = buttonData.getBorderColor(LocalAppColorScheme.current),
                    shape = RoundedCornerShape(LocalAppRadius.current.value100)
                ).heightIn(min = 48.dp, max = 48.dp)
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple,
                    role = Role.Button
                )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
            modifier =
                Modifier
                    .padding(
                        vertical = LocalAppSpacing.current.value200,
                        horizontal = LocalAppSpacing.current.value300
                    )
        ) {
            if (displayIcon && icon != null && !iconAlt.isNullOrBlank()) {
                Icon(
                    imageVector = icon,
                    contentDescription = iconAlt,
                    tint = buttonData.getForegroundColor(LocalAppColorScheme.current),
                    modifier = Modifier.size(24.dp)
                )
            }

            if (displayLabel && !label.isNullOrBlank()) {
                Text(
                    text = label,
                    color = buttonData.getForegroundColor(LocalAppColorScheme.current),
                    style = LocalAppTypography.current.bodyBase,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PrimaryButtonPreview() {
    OpenlysisTheme(darkTheme = false) {
        AppButton(
            buttonType = ButtonType.Danger,
            onClick = { },
            displayLabel = true,
            displayIcon = true,
            label = "Settings",
            icon = Icons.Outlined.Settings,
            iconAlt = "Settings icon."
        )
    }
}