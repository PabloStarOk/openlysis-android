package com.openlysis.feature.results.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.Tooltip
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.R
import com.openlysis.feature.results.components.AnalysisVerdictBadge
import com.openlysis.feature.results.components.VerdictStats
import com.openlysis.feature.results.components.VerdictStatsState
import kotlin.collections.forEach

/**
 * A section displaying the results of various services.
 *
 * @param isPrimarySection Indicates if this is a primary section in the current screen.
 * @param serviceResults List of service result data to display.
 * @param modifier Modifier for styling and layout.
 */
@Composable
internal fun ServiceResultsSection(
    isPrimarySection: Boolean,
    serviceResults: List<ServiceResultData>,
    modifier: Modifier = Modifier
) {
    var showHelpTooltip by rememberSaveable { mutableStateOf(false) }
    SectionAccordion(
        title = stringResource(R.string.details_screen_service_results_section_title),
        isPrimarySection = isPrimarySection,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            VerdictStats(
                state =
                    VerdictStatsState(
                        cleanVerdicts =
                            serviceResults.count { a -> a.verdict == Verdict.Undetected },
                        suspiciousVerdicts =
                            serviceResults.count { a -> a.verdict == Verdict.Suspicious },
                        maliciousVerdicts =
                            serviceResults.count { a -> a.verdict == Verdict.Malicious },
                        unknownVerdicts =
                            serviceResults.count { a -> a.verdict == Verdict.Unknown }
                    ),
                label = stringResource(R.string.verdict_stats_default_label),
                smallSize = true,
                showUnknown = true,
                modifier = Modifier.width(IntrinsicSize.Min)
            )

            if (serviceResults.any { it.threatScore != null }) {
                Box {
                    AppButton(
                        type = ButtonType.Tertiary,
                        size = SizeType.Small,
                        onClick = { showHelpTooltip = true },
                        displayLabel = false,
                        displayIcon = true,
                        icon = AppIcons.Help,
                        iconAlt = stringResource(R.string.help_icon_alt)
                    )

                    Tooltip(
                        onDismissRequest = { showHelpTooltip = false },
                        visible = showHelpTooltip,
                        messages =
                            arrayOf(
                                AnnotatedString(
                                    stringResource(
                                        R.string.details_screen_service_results_help_message
                                    )
                                )
                            )
                    )
                }
            }
        }

        serviceResults.forEach {
            ServiceResultItem(
                serviceName = it.serviceName,
                status = it.status,
                verdict = it.verdict,
                threatScore = it.threatScore
            )
        }

        if (serviceResults.isEmpty()) {
            Text(
                text = stringResource(R.string.details_screen_service_results_not_found),
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.default.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ServiceResultItem(
    serviceName: String,
    status: AnalysisStatus?,
    verdict: Verdict,
    threatScore: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement =
            Arrangement.spacedBy(
                LocalAppSpacing.current.value200
            ),
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = LocalAppColorScheme.current.border.default.primary,
                    shape =
                        RoundedCornerShape(
                            LocalAppRadius.current.value100
                        )
                ).padding(
                    vertical = LocalAppSpacing.current.value150,
                    horizontal = LocalAppSpacing.current.value200
                ).fillMaxWidth()
    ) {
        Text(
            text = serviceName,
            style = LocalAppTypography.current.bodySmall,
            color = LocalAppColorScheme.current.text.default.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        if (status == AnalysisStatus.Failed ||
            status == AnalysisStatus.Timeout
        ) {
            Text(
                text = "Analysis failed",
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.danger.secondary
            )
            return@Row
        }

        if (threatScore != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value100),
                modifier =
                    Modifier
                        .width(IntrinsicSize.Min)
                        .background(
                            color = LocalAppColorScheme.current.background.default.secondary,
                            shape =
                                RoundedCornerShape(
                                    LocalAppRadius.current.value100
                                )
                        ).border(
                            width = 1.dp,
                            color = LocalAppColorScheme.current.border.default.primary,
                            shape =
                                RoundedCornerShape(
                                    LocalAppRadius.current.value100
                                )
                        ).padding(
                            horizontal = LocalAppSpacing.current.value100
                        )
            ) {
                Text(
                    text = stringResource(R.string.details_screen_service_results_threat_score),
                    style = LocalAppTypography.current.bodySmallStrong,
                    color =
                        LocalAppColorScheme.current.text.default.primary
                )
                Text(
                    text = threatScore.toString(),
                    style = LocalAppTypography.current.bodySmall,
                    color =
                        LocalAppColorScheme.current.text.default.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        AnalysisVerdictBadge(
            verdict = verdict,
            size = SizeType.ExtraSmall,
            modifier = Modifier.width(IntrinsicSize.Min)
        )
    }
}