package com.openlysis.feature.results.components

import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.R
import kotlinx.datetime.toKotlinInstant
import java.time.Instant

/**
 * A composable that displays a preview of an analysis with interactive elements.
 *
 * @param onDetailsClick Callback invoked when the details button is clicked
 * @param onRefreshClick Callback invoked when the refresh button is clicked
 * @param headerLabel The label to display in the header section
 * @param state The current state of the analysis preview
 * @param isRefreshing Boolean indicating whether the preview is currently refreshing
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
internal fun AnalysisPreview(
    onDetailsClick: () -> Unit,
    onRefreshClick: () -> Unit,
    headerLabel: String,
    state: AnalysisPreviewState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current
    val formattedDate =
        remember(state.startedDate) {
            val epochMilliseconds =
                state.startedDate
                    .toKotlinInstant()
                    .toEpochMilliseconds()
            DateFormat
                .getMediumDateFormat(localContext)
                .format(epochMilliseconds)
        }

    val showRefreshButton =
        remember(state.status) {
            state.status == AnalysisStatus.Queued ||
                state.status == AnalysisStatus.InProgress
        }

    Card(
        colors =
            CardDefaults
                .cardColors()
                .copy(containerColor = Color.Transparent),
        border =
            BorderStroke(
                width = 1.dp,
                color = LocalAppColorScheme.current.border.brand.primary
            ),
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        modifier = modifier
    ) {
        Column(
            modifier =
                Modifier
                    .background(LocalAppColorScheme.current.background.brand.tertiary)
                    .border(width = 1.dp, color = LocalAppColorScheme.current.border.brand.primary)
                    .padding(
                        vertical = LocalAppSpacing.current.value300,
                        horizontal = LocalAppSpacing.current.value400
                    ).fillMaxWidth()
        ) {
            Text(
                text = headerLabel,
                style = LocalAppTypography.current.bodyXSmall,
                color = LocalAppColorScheme.current.text.default.secondary
            )

            Text(
                text = state.headerContent,
                style = LocalAppTypography.current.bodyBaseStrong,
                color = LocalAppColorScheme.current.text.default.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
            modifier =
                Modifier
                    .padding(LocalAppSpacing.current.value400)
                    .fillMaxWidth()
        ) {
            AnalysisStatusBadge(
                status = state.status,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
            ) {
                PreviewInfo(
                    label = stringResource(R.string.analysis_preview_started_date_label),
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                ) {
                    Text(
                        text = formattedDate,
                        style = LocalAppTypography.current.bodySmall,
                        color = LocalAppColorScheme.current.text.default.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                PreviewInfo(
                    label = stringResource(R.string.analysis_preview_verdict_label),
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                ) {
                    AnalysisVerdictBadge(
                        verdict = state.verdict,
                        size = SizeType.Small
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showRefreshButton) {
                    RefreshButton(
                        onRefreshClick = onRefreshClick,
                        isRefreshing = isRefreshing,
                        displayLabel = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                AppButton(
                    type = ButtonType.Primary,
                    size = SizeType.Default,
                    onClick = onDetailsClick,
                    displayLabel = true,
                    displayIcon = true,
                    label = stringResource(R.string.analysis_preview_details_button_label),
                    icon = AppIcons.Document,
                    iconAlt =
                        stringResource(R.string.analysis_preview_details_button_icon_alt),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PreviewInfo(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value200,
                alignment = Alignment.CenterVertically
            ),
        modifier =
            modifier
                .clip(RoundedCornerShape(LocalAppRadius.current.value100))
                .background(LocalAppColorScheme.current.background.brand.tertiary)
                .padding(LocalAppSpacing.current.value200)
    ) {
        Text(
            text = label,
            style = LocalAppTypography.current.bodySmallStrong,
            color = LocalAppColorScheme.current.text.default.primary,
            modifier = Modifier.fillMaxWidth()
        )

        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@PreviewLightDark
@Composable
private fun AnalysisPreviewPreview() {
    OpenlysisTheme {
        AnalysisPreview(
            onDetailsClick = { },
            onRefreshClick = { },
            headerLabel = "Test",
            state =
                AnalysisPreviewState(
                    id = "Test",
                    headerContent = "A test",
                    status = AnalysisStatus.Queued,
                    startedDate = Instant.ofEpochSecond(1751766596),
                    verdict = Verdict.Undetected
                ),
            isRefreshing = false
        )
    }
}