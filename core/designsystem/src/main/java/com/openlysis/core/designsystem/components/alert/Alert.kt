package com.openlysis.core.designsystem.components.alert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

@Composable
fun Alert(
    type: AlertType,
    text: String,
    modifier: Modifier = Modifier
) {
    val data = AlertTypeDataMap.getValue(type)
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .clip(shape = RoundedCornerShape(LocalAppRadius.current.value100))
                .background(data.getBackgroundColor(LocalAppColorScheme.current))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
            modifier =
                Modifier
                    .padding(
                        vertical = LocalAppSpacing.current.value200,
                        horizontal = LocalAppSpacing.current.value300
                    )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(data.iconResId),
                contentDescription = stringResource(data.iconAlt),
                tint = data.getForegroundColor(LocalAppColorScheme.current),
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = text,
                color = data.getForegroundColor(LocalAppColorScheme.current),
                style = LocalAppTypography.current.bodySmall
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AlertPreview() {
    OpenlysisTheme(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
        ) {
            Alert(
                type = AlertType.Warning,
                text = "Warning example."
            )

            Alert(
                type = AlertType.Danger,
                text = "Danger example."
            )
        }
    }
}