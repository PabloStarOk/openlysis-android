package com.openlysis.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A single item in the application's navigation bar.
 *
 * @param onClick A lambda function that will be invoked when this navigation bar item is clicked.
 * @param icon The [ImageVector] to be displayed as the icon for this item.
 * @param iconAlt A string providing an alternative text description for the icon, used for accessibility.
 * @param label The text label to be displayed below the icon.
 * @param selected A boolean indicating whether this navigation bar item is currently selected.
 * @param enabled A boolean indicating whether this navigation bar item is enabled and can be clicked.
 * @param modifier An optional [Modifier] to be applied to the root `Surface` of this item.
 */
@Composable
internal fun AppNavBarItem(
    onClick: () -> Unit,
    icon: ImageVector,
    iconAlt: String,
    label: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = ripple(color = LocalAppColorScheme.current.background.brand.primary)
    val appColorScheme = LocalAppColorScheme.current
    val colors =
        remember(selected, appColorScheme) {
            if (selected) {
                Pair(
                    appColorScheme.background.brand.tertiary,
                    appColorScheme.text.brand.onTertiary
                )
            } else {
                Pair(
                    appColorScheme.background.default.primary,
                    appColorScheme.text.default.primary
                )
            }
        }

    val backgroundColor by animateColorAsState(
        colors.first,
        animationSpec = tween(durationMillis = 300)
    )

    val foregroundColor by animateColorAsState(
        colors.second,
        animationSpec = tween(durationMillis = 300)
    )

    Surface(
        color = backgroundColor,
        modifier =
            modifier
                .clip(RoundedCornerShape(LocalAppRadius.current.value100))
                .height(60.dp)
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = ripple,
                    role = Role.Tab
                )
    ) {
        Column(
            verticalArrangement =
                Arrangement
                    .spacedBy(
                        LocalAppSpacing.current.value150,
                        alignment = Alignment.CenterVertically
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(all = LocalAppSpacing.current.value150)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconAlt,
                tint = foregroundColor,
                modifier = Modifier.size(size = 24.dp)
            )
            Text(
                text = label,
                style = LocalAppTypography.current.bodySmall,
                color = foregroundColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun AppNavBarItemPreview() {
    OpenlysisTheme(darkTheme = false) {
        AppNavBarItem(
            onClick = { },
            icon = Icons.Outlined.Settings,
            iconAlt = "Settings icon.",
            label = "Settings",
            selected = false,
            enabled = true
        )
    }
}