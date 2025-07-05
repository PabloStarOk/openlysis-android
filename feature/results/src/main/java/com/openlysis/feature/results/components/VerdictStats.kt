package com.openlysis.feature.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.results.R

/**
 * A composable that displays statistics about verdicts.
 *
 * @param state The state object containing counts of different verdict types
 * @param smallSize Whether to use the small variant
 * @param modifier Optional modifier for the composable
 * @param showUnknown Whether to display the count of unknown verdicts
 */
@Composable
internal fun VerdictStats(
    state: VerdictStatsState,
    smallSize: Boolean,
    modifier: Modifier = Modifier,
    showUnknown: Boolean = false
) {
    val gap =
        if (smallSize) {
            LocalAppSpacing.current.value050
        } else {
            LocalAppSpacing.current.value200
        }

    val labelStyle =
        if (smallSize) {
            LocalAppTypography.current.bodyXSmall
        } else {
            LocalAppTypography.current.bodySmall
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value100),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.verdict_stats_label),
            style = labelStyle,
            color = LocalAppColorScheme.current.text.default.secondary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(gap)
        ) {
            VerdictStatsItem(
                label = stringResource(R.string.verdict_stats_clean_label),
                value = state.cleanVerdicts,
                foregroundColor = LocalAppColorScheme.current.text.positive.onSecondary,
                backgroundColor = LocalAppColorScheme.current.background.positive.secondary,
                smallSize = smallSize,
                modifier = Modifier.weight(1f)
            )

            VerdictStatsItem(
                label = stringResource(R.string.verdict_stats_suspicious_label),
                value = state.suspiciousVerdicts,
                foregroundColor = LocalAppColorScheme.current.text.warning.onSecondary,
                backgroundColor = LocalAppColorScheme.current.background.warning.secondary,
                smallSize = smallSize,
                modifier = Modifier.weight(1f)
            )

            VerdictStatsItem(
                label = stringResource(R.string.verdict_stats_malicious_label),
                value = state.maliciousVerdicts,
                foregroundColor = LocalAppColorScheme.current.text.danger.onSecondary,
                backgroundColor = LocalAppColorScheme.current.background.danger.secondary,
                smallSize = smallSize,
                modifier = Modifier.weight(1f)
            )

            if (showUnknown) {
                VerdictStatsItem(
                    label = stringResource(R.string.verdict_stats_unknown_label),
                    value = state.unknownVerdicts,
                    foregroundColor = LocalAppColorScheme.current.text.default.primary,
                    backgroundColor = LocalAppColorScheme.current.background.default.secondary,
                    smallSize = smallSize,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun VerdictStatsPreview() {
    OpenlysisTheme {
        VerdictStats(
            state =
                VerdictStatsState(
                    cleanVerdicts = 10,
                    suspiciousVerdicts = 2,
                    maliciousVerdicts = 3,
                    unknownVerdicts = 2
                ),
            smallSize = false,
            showUnknown = true
        )
    }
}