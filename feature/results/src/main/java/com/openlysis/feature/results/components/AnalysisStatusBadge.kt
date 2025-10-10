package com.openlysis.feature.results.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.color.AppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.feature.results.R

/**
 * A composable that displays a badge indicating the current status of an analysis.
 *
 * @param status The current status of the analysis to be displayed
 * @param modifier Optional [Modifier] to be applied to the badge
 */
@Composable
internal fun AnalysisStatusBadge(
    status: AnalysisStatus,
    modifier: Modifier = Modifier
) {
    val style = badgeStylesMap.getValue(status)
    val transition = updateTransition(targetState = style, label = "Status badge transition")
    val backgroundColor by
        transition.animateColor(
            transitionSpec = { tween(200) },
            label = "Status badge background color"
        ) {
            it.getBackgroundColor(LocalAppColorScheme.current)
        }
    val foregroundColor by
        transition.animateColor(
            transitionSpec = { tween(200) },
            label = "Status badge foreground color"
        ) {
            it.getForegroundColor(LocalAppColorScheme.current)
        }

    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
        modifier =
            modifier
                .widthIn(min = 333.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(LocalAppRadius.current.value100),
            color = backgroundColor,
            modifier =
                Modifier
                    .weight(1f)
        ) {
            Text(
                text = stringResource(style.labelResId),
                style = LocalAppTypography.current.bodySmall,
                color = foregroundColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier
                        .padding(LocalAppSpacing.current.value100)
                        .fillMaxWidth()
            )
        }

        Surface(
            color = style.getBackgroundColor(LocalAppColorScheme.current),
            shape = RoundedCornerShape(LocalAppRadius.current.value100)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(style.iconResId),
                contentDescription = stringResource(style.iconAltResId),
                tint = style.getForegroundColor(LocalAppColorScheme.current),
                modifier =
                    Modifier
                        .padding(LocalAppSpacing.current.value100)
                        .size(17.92.dp)
            )
        }
    }
}

private val badgeStylesMap =
    mapOf(
        Pair(
            AnalysisStatus.Queued,
            AnalysisStatusBadgeStyle(
                getBackgroundColor = { it.background.warning.tertiary },
                getForegroundColor = { it.text.warning.onTertiary },
                labelResId = R.string.analysis_status_queued_badge_label,
                iconResId = AppIconsIds.GitCommit,
                iconAltResId = R.string.analysis_status_queued_badge_icon_alt
            )
        ),
        Pair(
            AnalysisStatus.InProgress,
            AnalysisStatusBadgeStyle(
                getBackgroundColor = { it.background.warning.tertiary },
                getForegroundColor = { it.text.warning.onTertiary },
                labelResId = R.string.analysis_status_in_progress_badge_label,
                iconResId = AppIconsIds.RotateLeft,
                iconAltResId = R.string.analysis_status_in_progress_badge_icon_alt
            )
        ),
        Pair(
            AnalysisStatus.Completed,
            AnalysisStatusBadgeStyle(
                getBackgroundColor = { it.background.positive.tertiary },
                getForegroundColor = { it.text.positive.onTertiary },
                labelResId = R.string.analysis_status_completed_badge_label,
                iconResId = AppIconsIds.Check,
                iconAltResId = R.string.analysis_status_completed_badge_icon_alt
            )
        ),
        Pair(
            AnalysisStatus.Failed,
            AnalysisStatusBadgeStyle(
                getBackgroundColor = { it.background.danger.tertiary },
                getForegroundColor = { it.text.danger.onTertiary },
                labelResId = R.string.analysis_status_failed_badge_label,
                iconResId = AppIconsIds.Cross,
                iconAltResId = R.string.analysis_status_failed_badge_icon_alt
            )
        ),
        Pair(
            AnalysisStatus.Timeout,
            AnalysisStatusBadgeStyle(
                getBackgroundColor = { it.background.danger.tertiary },
                getForegroundColor = { it.text.danger.onTertiary },
                labelResId = R.string.analysis_status_timeout_badge_label,
                iconResId = AppIconsIds.Clock,
                iconAltResId = R.string.analysis_status_timeout_badge_icon_alt
            )
        )
    )

@Immutable
private data class AnalysisStatusBadgeStyle(
    val getBackgroundColor: (AppColorScheme) -> Color,
    val getForegroundColor: (AppColorScheme) -> Color,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
    @StringRes val iconAltResId: Int
)

@PreviewLightDark
@Composable
private fun AnalysisStatusBadgePreview() {
    OpenlysisTheme {
        AnalysisStatusBadge(
            status = AnalysisStatus.Queued
        )
    }
}