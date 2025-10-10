package com.openlysis.feature.results.components.preview

import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.openlysis.feature.results.components.AnalysisStatusBadge
import com.openlysis.feature.results.components.AnalysisVerdictBadge
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant

/**
 * A composable that displays a preview of an analysis with interactive elements.
 *
 * @param onDetailsClick Callback invoked when the details button is clicked
 * @param headerLabel The label to display in the header section
 * @param state The current state of the analysis preview
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
internal fun AnalysisPreview(
    onDetailsClick: () -> Unit,
    headerLabel: String,
    state: AnalysisPreviewState,
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current
    val formattedDate =
        remember(state.startedDate) {
            val epochMilliseconds = state.startedDate.toEpochMilliseconds()
            DateFormat
                .getMediumDateFormat(localContext)
                .format(epochMilliseconds)
        }

    Surface(
        color = LocalAppColorScheme.current.background.default.primary,
        border =
            BorderStroke(
                width = 1.dp,
                color = LocalAppColorScheme.current.border.default.primary
            ),
        shape = RoundedCornerShape(LocalAppRadius.current.value200),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = LocalAppSpacing.current.value400)
        ) {
            PreviewHeader(
                headerLabel = headerLabel,
                headerContent = state.headerContent,
                isRefreshing = state.isRefreshing,
                isUpdatable = state.isRefreshable(),
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

            PreviewBody(
                analysisStatus = state.status,
                date = formattedDate,
                verdict = state.verdict,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

            PreviewButtons(
                onDetailsClick = onDetailsClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PreviewHeader(
    headerLabel: String,
    headerContent: String,
    isRefreshing: Boolean,
    isUpdatable: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        modifier = modifier.padding(vertical = LocalAppSpacing.current.value300)
    ) {
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = headerLabel,
                style = LocalAppTypography.current.bodyXSmall,
                color = LocalAppColorScheme.current.text.default.secondary
            )

            Text(
                text = headerContent,
                style = LocalAppTypography.current.bodyBase,
                color = LocalAppColorScheme.current.text.default.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        PreviewHeaderIndicationIcons(isRefreshing, isUpdatable)
    }
}

@Composable
private fun PreviewHeaderIndicationIcons(
    isRefreshing: Boolean,
    isUpdatable: Boolean,
    modifier: Modifier = Modifier
) {
    val indicationIconModifier = Modifier.sizeIn(maxWidth = 24.dp, maxHeight = 24.dp)
    var showCompletedIcon by rememberSaveable { mutableStateOf(false) }
    var wasRefreshing by rememberSaveable { mutableStateOf(isRefreshing) }

    LaunchedEffect(isRefreshing, isUpdatable) {
        val isJustCompleted = wasRefreshing && !isRefreshing && !isUpdatable
        if (isJustCompleted) {
            showCompletedIcon = true
            delay(5000)
            showCompletedIcon = false
        }
        wasRefreshing = isRefreshing
    }

    AnimatedContent(
        targetState = Triple(isRefreshing, isUpdatable, showCompletedIcon),
        transitionSpec = { scaleIn() + fadeIn() togetherWith fadeOut() + scaleOut() },
        modifier = modifier
    ) { (refreshing, updatable, completed) ->
        when {
            refreshing ->
                CircularProgressIndicator(
                    color = LocalAppColorScheme.current.icon.brand.primary,
                    trackColor = LocalAppColorScheme.current.border.default.primary,
                    modifier = indicationIconModifier
                )
            completed ->
                Icon(
                    imageVector = AppIcons.Check,
                    contentDescription =
                        stringResource(R.string.previews_cards_header_icon_check_alt),
                    tint = LocalAppColorScheme.current.icon.positive.secondary,
                    modifier = indicationIconModifier
                )
            updatable ->
                Icon(
                    imageVector = AppIcons.CloudOff,
                    contentDescription =
                        stringResource(R.string.previews_cards_header_icon_cloud_off_alt),
                    tint = LocalAppColorScheme.current.icon.default.tertiary,
                    modifier = indicationIconModifier
                )
        }
    }
}

@Composable
private fun PreviewBody(
    analysisStatus: AnalysisStatus,
    date: String,
    verdict: Verdict,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        modifier = modifier.padding(vertical = LocalAppSpacing.current.value400)
    ) {
        AnalysisStatusBadge(
            status = analysisStatus,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
        ) {
            PreviewBodyInfoCard(
                label = stringResource(R.string.analysis_preview_started_date_label),
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
            ) {
                Text(
                    text = date,
                    style = LocalAppTypography.current.bodySmall,
                    color = LocalAppColorScheme.current.text.default.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            PreviewBodyInfoCard(
                label = stringResource(R.string.analysis_preview_verdict_label),
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
            ) {
                AnalysisVerdictBadge(
                    verdict = verdict,
                    size = SizeType.Small
                )
            }
        }
    }
}

@Composable
private fun PreviewButtons(
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        modifier = modifier.padding(vertical = LocalAppSpacing.current.value100)
    ) {
        AppButton(
            type = ButtonType.Tertiary,
            size = SizeType.Small,
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

@Composable
private fun PreviewBodyInfoCard(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = LocalAppTypography.current.bodyXSmall,
            color = LocalAppColorScheme.current.text.default.secondary,
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
            headerLabel = "Test",
            state =
                AnalysisPreviewState(
                    id = "Test",
                    headerContent = "A test",
                    status = AnalysisStatus.Queued,
                    startedDate = Instant.fromEpochSeconds(1751766596),
                    verdict = Verdict.Undetected,
                    isRefreshing = true
                )
        )
    }
}