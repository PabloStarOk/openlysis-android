package com.openlysis.feature.tools.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A clickable card that displays information about an available analysis tool.
 *
 * @param label The primary text displayed on the card, acting as a title.
 * @param description A short explanation of the tool's purpose or functionality.
 * @param icon The [ImageVector] to be displayed as an icon on the card.
 * @param iconAlt A textual description of the icon, used for accessibility purposes.
 * @param onClick A lambda function that will be executed when the card is clicked.
 * @param modifier An optional [Modifier] to be applied to the card for custom styling or layout. Defaults to [Modifier].
 */
@Composable
internal fun ToolCard(
    label: String,
    description: String,
    icon: ImageVector,
    iconAlt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = ripple(color = LocalAppColorScheme.current.background.brand.primary)

    Surface(
        color = LocalAppColorScheme.current.background.default.primary,
        border = BorderStroke(1.dp, LocalAppColorScheme.current.border.brand.primary),
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        modifier =
            modifier
                .height(IntrinsicSize.Min)
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = ripple,
                    role = Role.Button
                )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .padding(
                        LocalAppSpacing.current.value600
                    ).fillMaxHeight()
                    .height(IntrinsicSize.Max)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
            ) {
                Text(
                    text = label,
                    style = LocalAppTypography.current.bodyBaseStrong,
                    color = LocalAppColorScheme.current.text.brand.primary,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = description,
                    style = LocalAppTypography.current.bodySmall,
                    color = LocalAppColorScheme.current.text.default.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Icon(
                icon,
                contentDescription = iconAlt,
                tint = LocalAppColorScheme.current.icon.default.primary,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ToolCardPreview() {
    OpenlysisTheme(darkTheme = false) {
        ToolCard(
            label = "Label",
            description = "Description",
            icon = Icons.Outlined.Email,
            iconAlt = "Icon of a mail",
            { }
        )
    }
}