package com.openlysis.feature.results.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.results.R

/**
 * A composable function that displays a clickable card with stats about current verdicts.
 *
 * @param onClick Callback to be invoked when the card is clicked
 * @param title The title text to be displayed in the card header
 * @param description The descriptive text to be displayed below the title
 * @param verdictStatsState The state object containing verdict statistics to be displayed
 * @param modifier Optional modifier for the card layout (defaults to [Modifier])
 */
@Composable
internal fun AnalysisResultsCard(
    onClick: () -> Unit,
    title: String,
    description: String,
    verdictStatsState: VerdictStatsState,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent),
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        border =
            BorderStroke(
                width = 1.dp,
                color = LocalAppColorScheme.current.border.brand.tertiary
            ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
            modifier = Modifier.padding(LocalAppSpacing.current.value400)
        ) {
            Column(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(LocalAppRadius.current.value100))
                        .background(LocalAppColorScheme.current.background.brand.tertiary)
                        .padding(
                            vertical = LocalAppSpacing.current.value300,
                            horizontal = LocalAppSpacing.current.value400
                        ).fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = title,
                        style = LocalAppTypography.current.title5,
                        color = LocalAppColorScheme.current.text.brand.onTertiary,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = AppIcons.ArrowRight,
                        contentDescription =
                            stringResource(
                                R.string.analysis_results_section_card_icon_alt
                            ),
                        tint = LocalAppColorScheme.current.icon.brand.onTertiary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = description,
                    style = LocalAppTypography.current.bodyBase,
                    color = LocalAppColorScheme.current.text.default.primary
                )
            }

            VerdictStats(
                state = verdictStatsState,
                label = stringResource(R.string.verdict_stats_last_results_label),
                smallSize = false,
                modifier =
                    Modifier
                        .padding(LocalAppSpacing.current.value100)
                        .fillMaxWidth()
            )
        }
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
            verdictStatsState = VerdictStatsState.Zero
        )
    }
}