package com.openlysis.feature.tools.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.ui.theme.OpenlysisTheme
import com.openlysis.core.ui.theme.size.Spacing
import com.openlysis.core.ui.theme.type.Body

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
    val isPressed = interactionSource.collectIsPressedAsState()

    val containerColor by
        GetColorState(
            isPressed = isPressed,
            defaultColor = MaterialTheme.colorScheme.background,
            pressedColor = MaterialTheme.colorScheme.surface
        )

    val borderColor by
        GetColorState(
            isPressed = isPressed,
            defaultColor = MaterialTheme.colorScheme.outline,
            pressedColor = Color.Unspecified
        )

    val labelColor by
        GetColorState(
            isPressed = isPressed,
            defaultColor = MaterialTheme.colorScheme.onBackground,
            pressedColor = MaterialTheme.colorScheme.onSurface
        )

    Card(
        modifier =
            modifier
                .height(IntrinsicSize.Min),
        colors =
            CardColors(
                containerColor = containerColor,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.onBackground
            ),
        border = BorderStroke(1.dp, borderColor),
        onClick = onClick,
        interactionSource = interactionSource
    ) {
        Row(
            modifier =
                Modifier
                    .padding(
                        Spacing.Value400
                    ).fillMaxHeight()
                    .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Value400),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = Body.BaseStrong,
                    color = labelColor,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Icon(
                icon,
                contentDescription = iconAlt,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun GetColorState(
    isPressed: State<Boolean>,
    defaultColor: Color,
    pressedColor: Color
): State<Color> =
    animateColorAsState(
        if (isPressed.value) pressedColor else defaultColor,
        animationSpec = tween(durationMillis = 200)
    )

@Preview(showSystemUi = true)
@Composable
private fun ToolCardPreview() {
    OpenlysisTheme(darkTheme = false) {
        ToolCard(
            label = "Label",
            description = "Description",
            Icons.Outlined.Email,
            "Icon of a mail",
            { }
        )
    }
}