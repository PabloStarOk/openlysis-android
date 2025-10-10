package com.openlysis.feature.results.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.results.R

/**
 * An information card with a label and information text.
 *
 * @param label The label to display at the top of the card.
 * @param information The main information content to display.
 * @param modifier Modifier to be applied to the card.
 * @param showCopyButton Whether to show a copy button next to the label.
 * @param onCopyClick Callback invoked when the copy button is clicked.
 */
@Composable
internal fun InformationCard(
    label: String,
    information: String,
    modifier: Modifier = Modifier,
    showCopyButton: Boolean = false,
    onCopyClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(LocalAppRadius.current.value100)
    Box(
        modifier =
            modifier
                .clip(shape)
                .background(
                    color = LocalAppColorScheme.current.background.default.primary,
                    shape = shape
                ).border(
                    width = 1.dp,
                    color = LocalAppColorScheme.current.border.default.primary,
                    shape = shape
                )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value050),
            modifier = Modifier.padding(LocalAppSpacing.current.value300)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = LocalAppTypography.current.bodySmall,
                    color = LocalAppColorScheme.current.text.default.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (showCopyButton && onCopyClick != null) {
                    AppButton(
                        type = ButtonType.Tertiary,
                        size = SizeType.Small,
                        onClick = onCopyClick,
                        displayLabel = false,
                        displayIcon = true,
                        icon = AppIcons.Copy,
                        iconAlt = stringResource(R.string.copy_icon_alt)
                    )
                }
            }

            Text(
                text = information,
                style = LocalAppTypography.current.bodyBase,
                color = LocalAppColorScheme.current.text.default.primary
            )
        }
    }
}