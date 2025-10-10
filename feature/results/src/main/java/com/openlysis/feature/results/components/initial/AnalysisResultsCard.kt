package com.openlysis.feature.results.components.initial

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.results.R
import com.openlysis.feature.results.components.VerdictStats
import com.openlysis.feature.results.components.VerdictStatsState

/**
 * A composable function that displays a clickable card with stats about current verdicts.
 *
 * @param onClick Callback to be invoked when the card is clicked
 * @param title The title text to be displayed in the card header
 * @param description The descriptive text to be displayed below the title
 * @param icon The icon to use in the header of the card.
 * @param iconAlt The alternative text to describe the icon of the card's header.
 * @param verdictStatsState The state object containing verdict statistics to be displayed
 * @param analysesInProgress The analyses that are still in progress.
 * @param modifier Optional modifier for the card layout (defaults to [Modifier])
 */
@Composable
internal fun AnalysisResultsCard(
    onClick: () -> Unit,
    title: String,
    description: String,
    icon: ImageVector,
    iconAlt: String,
    verdictStatsState: VerdictStatsState,
    analysesInProgress: Int,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = ripple(color = LocalAppColorScheme.current.background.brand.primary)

    Surface(
        color = LocalAppColorScheme.current.background.default.primary,
        border =
            BorderStroke(
                width = 1.dp,
                color = LocalAppColorScheme.current.border.default.primary
            ),
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        shadowElevation = 1.dp,
        modifier =
            modifier.clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple,
                role = Role.Button
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
            modifier = Modifier.padding(LocalAppSpacing.current.value400)
        ) {
            ResultsCardHeader(
                title = title,
                description = description,
                icon = icon,
                iconAlt = iconAlt,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
                modifier = Modifier.fillMaxWidth()
            ) {
                VerdictStats(
                    state = verdictStatsState,
                    label = stringResource(R.string.verdict_stats_last_results_label),
                    smallSize = false,
                    modifier = Modifier.weight(1f)
                )

                AnalysesInProgress(
                    amount = analysesInProgress,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ResultsCardHeader(
    title: String,
    description: String,
    icon: ImageVector,
    iconAlt: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        modifier = modifier
    ) {
        ResultsCardHeaderIcon(
            icon = icon,
            iconAlt = iconAlt
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value050),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value050)
            ) {
                Text(
                    text = title,
                    style = LocalAppTypography.current.title6,
                    color = LocalAppColorScheme.current.text.brand.primary,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = AppIcons.ChevronRight,
                    contentDescription =
                        stringResource(R.string.analysis_results_section_card_icon_alt),
                    tint = LocalAppColorScheme.current.icon.brand.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = description,
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.default.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResultsCardHeaderIcon(
    icon: ImageVector,
    iconAlt: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .background(
                    color = LocalAppColorScheme.current.background.brand.tertiary,
                    shape = RoundedCornerShape(LocalAppRadius.current.full)
                ).padding(LocalAppSpacing.current.value200)
    ) {
        Icon(
            icon,
            contentDescription = iconAlt,
            tint = LocalAppColorScheme.current.icon.brand.primary,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun AnalysesInProgress(
    amount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value100),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.analysis_results_section_card_in_progress),
            style = LocalAppTypography.current.bodyXSmall,
            color = LocalAppColorScheme.current.text.default.secondary
        )

        Text(
            text = amount.toString(),
            style = LocalAppTypography.current.bodySmall,
            color = LocalAppColorScheme.current.text.default.primary
        )
    }
}

@PreviewLightDark
@Composable
private fun AnalysisResultsCardPreview() {
    OpenlysisTheme {
        AnalysisResultsCard(
            onClick = { },
            title = "Title",
            description = "Example description.",
            icon = AppIcons.Mail,
            iconAlt = "Mail icon",
            verdictStatsState = VerdictStatsState.Companion.Zero,
            analysesInProgress = 16
        )
    }
}