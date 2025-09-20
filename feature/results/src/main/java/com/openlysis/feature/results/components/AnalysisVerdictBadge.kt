package com.openlysis.feature.results.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.color.AppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.size.Spacing
import com.openlysis.core.designsystem.theme.type.AppTypography
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.R

/**
 * A composable function that renders a badge displaying the analysis verdict.
 *
 * @param verdict The verdict to display
 * @param size The size variant of the badge
 * @param modifier Optional [Modifier] to be applied to the badge
 */
@Composable
internal fun AnalysisVerdictBadge(
    verdict: Verdict,
    size: SizeType,
    modifier: Modifier = Modifier
) {
    val style = badgeStylesMap.getValue(verdict)
    val dimensions = badgeDimensionsMap.getValue(size)
    val transition = updateTransition(targetState = style, "Verdict badge transition")
    val backGroundColor by transition.animateColor(
        transitionSpec = { tween(200) },
        label = "Verdict badge background color"
    ) {
        it.getBackgroundColor(LocalAppColorScheme.current)
    }
    val borderColor by transition.animateColor(
        transitionSpec = { tween(200) },
        label = "Verdict badge border color"
    ) {
        it.getBorderColor(LocalAppColorScheme.current)
    }
    val foregroundColor by transition.animateColor(
        transitionSpec = { tween(200) },
        label = "Verdict badge foreground color"
    ) {
        it.getForegroundColor(LocalAppColorScheme.current)
    }

    Surface(
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        color = backGroundColor,
        border = BorderStroke(width = 1.dp, color = borderColor),
        modifier = modifier.widthIn(min = dimensions.minWidth)
    ) {
        Text(
            text = stringResource(style.labelResId),
            style = dimensions.getLabelStyle(LocalAppTypography.current),
            color = foregroundColor,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(
                        vertical = dimensions.getVerticalPadding(LocalAppSpacing.current),
                        horizontal = dimensions.getHorizontalPadding(LocalAppSpacing.current)
                    ).fillMaxWidth()
        )
    }
}

private val badgeStylesMap =
    mapOf(
        Pair(
            Verdict.Undetected,
            AnalysisVerdictBadgeStyle(
                getBackgroundColor = { it.background.positive.tertiary },
                getBorderColor = { it.border.positive.primary },
                getForegroundColor = { it.text.positive.onTertiary },
                labelResId = R.string.analysis_verdict_clean_badge
            )
        ),
        Pair(
            Verdict.Suspicious,
            AnalysisVerdictBadgeStyle(
                getBackgroundColor = { it.background.warning.tertiary },
                getBorderColor = { it.border.warning.primary },
                getForegroundColor = { it.text.warning.onTertiary },
                labelResId = R.string.analysis_verdict_suspicious_badge
            )
        ),
        Pair(
            Verdict.Malicious,
            AnalysisVerdictBadgeStyle(
                getBackgroundColor = { it.background.danger.tertiary },
                getBorderColor = { it.border.danger.primary },
                getForegroundColor = { it.text.danger.onTertiary },
                labelResId = R.string.analysis_verdict_malicious_badge
            )
        ),
        Pair(
            Verdict.Unknown,
            AnalysisVerdictBadgeStyle(
                getBackgroundColor = { it.background.default.secondary },
                getBorderColor = { it.border.default.secondary },
                getForegroundColor = { it.text.default.secondary },
                labelResId = R.string.analysis_verdict_unknown_badge
            )
        )
    )

private val badgeDimensionsMap =
    mapOf(
        Pair(
            SizeType.Default,
            AnalysisVerdictBadgeDimensions(
                minWidth = 133.dp,
                getVerticalPadding = { it.value150 },
                getHorizontalPadding = { it.value300 },
                getLabelStyle = { it.bodyBase }
            )
        ),
        Pair(
            SizeType.Large,
            AnalysisVerdictBadgeDimensions(
                minWidth = 163.dp,
                getVerticalPadding = { it.value200 },
                getHorizontalPadding = { it.value400 },
                getLabelStyle = { it.bodyLarge }
            )
        ),
        Pair(
            SizeType.Small,
            AnalysisVerdictBadgeDimensions(
                minWidth = 108.dp,
                getVerticalPadding = { it.value100 },
                getHorizontalPadding = { it.value200 },
                getLabelStyle = { it.bodySmall }
            )
        ),
        Pair(
            SizeType.ExtraSmall,
            AnalysisVerdictBadgeDimensions(
                minWidth = 91.dp,
                getVerticalPadding = { it.value050 },
                getHorizontalPadding = { it.value150 },
                getLabelStyle = { it.bodyXSmall }
            )
        )
    )

@Immutable
internal data class AnalysisVerdictBadgeStyle(
    val getBackgroundColor: (AppColorScheme) -> Color,
    val getForegroundColor: (AppColorScheme) -> Color,
    val getBorderColor: (AppColorScheme) -> Color,
    @StringRes val labelResId: Int
)

@Immutable
internal data class AnalysisVerdictBadgeDimensions(
    val minWidth: Dp,
    val getVerticalPadding: (Spacing) -> Dp,
    val getHorizontalPadding: (Spacing) -> Dp,
    val getLabelStyle: (AppTypography) -> TextStyle
)

@PreviewLightDark
@Composable
private fun AnalysisVerdictBadgePreview() {
    OpenlysisTheme {
        AnalysisVerdictBadge(
            verdict = Verdict.Undetected,
            size = SizeType.Default
        )
    }
}