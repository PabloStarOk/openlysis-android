package com.openlysis.feature.tools.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.ui.theme.OpenlysisTheme
import com.openlysis.core.ui.theme.size.Spacing

@Composable
internal fun ToolCard(
    label: String,
    description: String,
    icon: ImageVector,
    iconAlt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier =
            modifier
                .height(IntrinsicSize.Min),
        colors =
            CardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.onBackground
            ),
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                    style = MaterialTheme.typography.bodyMedium,
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