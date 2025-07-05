package com.openlysis.feature.results.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A composable that displays a statistics item with a label and an animated value.
 *
 * @param label The text label to display below the value (only shown when smallSize is false)
 * @param value The numeric value to display with animation
 * @param foregroundColor The color for the text elements
 * @param backgroundColor The background color of the item
 * @param smallSize Whether to use a compact layout with smaller text and padding
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
internal fun VerdictStatsItem(
    label: String,
    value: Int,
    foregroundColor: Color,
    backgroundColor: Color,
    smallSize: Boolean,
    modifier: Modifier = Modifier
) {
    val verticalPadding =
        if (smallSize) {
            LocalAppSpacing.current.value150
        } else {
            LocalAppSpacing.current.value050
        }

    val valueStyle =
        if (smallSize) {
            LocalAppTypography.current.bodyXSmall
        } else {
            LocalAppTypography.current.bodyBaseStrong
        }

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value150,
                alignment = Alignment.CenterVertically
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .clip(shape = RoundedCornerShape(LocalAppRadius.current.value100))
                .background(backgroundColor)
                .padding(
                    vertical = verticalPadding,
                    horizontal = LocalAppSpacing.current.value100
                )
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { it } + fadeIn() togetherWith
                        slideOutVertically { -it } + fadeOut()
                } else {
                    slideInVertically { -it } + fadeIn() togetherWith
                        slideOutVertically { it } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { state ->
            Text(
                text = state.toString(),
                style = valueStyle,
                color = foregroundColor
            )
        }

        if (!smallSize) {
            Text(
                text = label,
                style = LocalAppTypography.current.bodySmall,
                color = foregroundColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun VerdictStatsItemPreview() {
    OpenlysisTheme {
        VerdictStatsItem(
            label = "Clean",
            value = 5,
            foregroundColor = LocalAppColorScheme.current.text.positive.onSecondary,
            backgroundColor = LocalAppColorScheme.current.background.positive.secondary,
            smallSize = false
        )
    }
}