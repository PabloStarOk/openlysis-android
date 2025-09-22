package com.openlysis.feature.results.components.detail

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.Tooltip
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.DetailsUiState
import com.openlysis.feature.results.R
import com.openlysis.feature.results.components.AnalysisStatusBadge
import com.openlysis.feature.results.components.AnalysisVerdictBadge
import com.openlysis.feature.results.util.getNetworkErrorMessage
import kotlinx.coroutines.delay
import kotlinx.datetime.toKotlinInstant

/**
 * Scaffold for displaying details screens.
 *
 * @param onTopBarUpdate Callback to update the top bar state
 * @param onLoadDetails Callback to load the result details to display
 * @param screenTitle Title to be displayed in the top bar
 * @param uiState Current UI state of the details screen
 * @param data Data to be displayed in the scaffold
 * @param modifier Optional modifier for customizing the layout
 * @param content Custom content to be displayed within the scaffold when the [TResult] of the [DetailsUiState.Success] is not null.
 */
@Composable
internal fun <TResult : Model> DetailsScreenScaffold(
    onTopBarUpdate: (TopBarState) -> Unit,
    onLoadDetails: () -> Unit,
    screenTitle: String,
    uiState: DetailsUiState<TResult>,
    data: DetailsScreenScaffoldData?,
    modifier: Modifier = Modifier,
    content: @Composable (TResult) -> Unit
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    if (!initialized) {
        initialized = true
        LaunchedEffect(Unit) {
            onLoadDetails()
        }
    }

    LaunchedEffect(Unit) {
        onTopBarUpdate(TopBarState(title = screenTitle))
    }

    val scrollState = rememberScrollState()
    val showStatusBar by remember(uiState, data) {
        derivedStateOf {
            uiState is DetailsUiState.Success && uiState.isRefreshable
        }
    }
    val statusBarExitDelay = 5000
    var statusBarReservedHeight by rememberSaveable(showStatusBar) {
        mutableIntStateOf(0)
    }
    val refreshingError by remember(uiState) {
        derivedStateOf {
            showStatusBar &&
                uiState is DetailsUiState.Success && !uiState.isRefreshing
        }
    }

    LaunchedEffect(showStatusBar) {
        if (!showStatusBar) {
            delay(statusBarExitDelay.toLong())
            statusBarReservedHeight = 0
        }
    }

    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .verticalScroll(scrollState)
                    .padding(LocalAppSpacing.current.value400)
                    .fillMaxHeight(1f)
                    .padding()
        ) {
            if (uiState is DetailsUiState.Loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        color = LocalAppColorScheme.current.icon.brand.primary,
                        trackColor = LocalAppColorScheme.current.border.default.primary,
                        modifier = Modifier.size(50.dp)
                    )
                }
            } else if (uiState is DetailsUiState.Failure) {
                val error = uiState.error
                Text(
                    text = getNetworkErrorMessage(error),
                    color = LocalAppColorScheme.current.text.danger.secondary,
                    style = LocalAppTypography.current.bodyBase,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                return@Column
            }

            if (uiState !is DetailsUiState.Success || data == null) {
                return@Column
            }

            val dateFormat = DateFormat.getLongDateFormat(LocalContext.current)
            val timeFormat = DateFormat.getTimeFormat(LocalContext.current)
            val startedDateMillis = data.startedDate.toKotlinInstant().toEpochMilliseconds()
            val formattedDate = dateFormat.format(startedDateMillis)
            val formattedTime = timeFormat.format(startedDateMillis)

            HeroInformation(
                status = data.status,
                verdict = data.verdict,
                threatScore = data.threatScore,
                showHeroDataInfoCard = data.heroInfoCardData != null,
                heroDataInfoCardLabel = data.heroInfoCardData?.first,
                heroDataInfoCardContent = data.heroInfoCardData?.second
            )

            SectionAccordion(
                title = stringResource(R.string.details_screen_started_datetime_section_title),
                initiallyExpanded = true
            ) {
                InformationCard(
                    label =
                        stringResource(R.string.details_screen_started_datetime_section_date_label),
                    information = formattedDate
                )

                InformationCard(
                    label =
                        stringResource(
                            R.string.details_screen_started_datetime_section_time_label
                        ),
                    information = formattedTime
                )
            }

            if (data.informationSectionTitle != null && data.informationSectionItems != null) {
                SectionAccordion(
                    title = data.informationSectionTitle,
                    initiallyExpanded = true
                ) {
                    data.informationSectionItems.forEach {
                        InformationCard(
                            label = it.first,
                            information = it.second
                        )
                    }
                }
            }

            content(uiState.analysis)

            Spacer(
                modifier =
                    modifier
                        .animateContentSize(animationSpec = tween(300))
                        .height(with(LocalDensity.current) { statusBarReservedHeight.toDp() })
            )
        }

        if (data == null) return@Box

        AnimatedVisibility(
            visible = showStatusBar,
            enter = fadeIn(tween(300)) + scaleIn(tween(400)),
            exit =
                fadeOut(tween(300, delayMillis = statusBarExitDelay)) +
                    scaleOut(tween(400, delayMillis = statusBarExitDelay)),
            modifier =
                Modifier
                    .padding(LocalAppSpacing.current.value400)
                    .align(Alignment.BottomCenter)
        ) {
            StatusIndicationBar(
                status = data.status,
                refreshingError = refreshingError,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onSizeChanged { statusBarReservedHeight = it.height }
            )
        }
    }
}

@Composable
private fun HeroInformation(
    status: AnalysisStatus,
    verdict: Verdict,
    threatScore: Int?,
    showHeroDataInfoCard: Boolean,
    heroDataInfoCardLabel: String?,
    heroDataInfoCardContent: String?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
        modifier = modifier
    ) {
        AnalysisStatusBadge(
            status = status,
            modifier = Modifier.fillMaxWidth()
        )

        if (showHeroDataInfoCard &&
            heroDataInfoCardLabel != null &&
            heroDataInfoCardContent != null
        ) {
            HeroInformationCard(
                label = heroDataInfoCardLabel,
                showHelpButton = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = heroDataInfoCardContent,
                    style = LocalAppTypography.current.bodyBase,
                    color = LocalAppColorScheme.current.text.default.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            HeroInformationCard(
                label = stringResource(R.string.details_screen_verdict_label),
                showHelpButton = true,
                helpMessage = stringResource(R.string.details_screen_verdict_help),
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
            ) {
                AnalysisVerdictBadge(
                    verdict = verdict,
                    size = SizeType.Default
                )
            }

            HeroInformationCard(
                label = stringResource(R.string.details_screen_threat_score_label),
                showHelpButton = true,
                helpMessage = stringResource(R.string.details_screen_threat_score_help),
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .background(
                                color = LocalAppColorScheme.current.background.default.primary,
                                shape = RoundedCornerShape(LocalAppRadius.current.value100)
                            ).padding(
                                vertical = LocalAppSpacing.current.value100,
                                horizontal = LocalAppSpacing.current.value200
                            ).fillMaxWidth()
                            .weight(1f)
                ) {
                    Text(
                        text = "${threatScore ?: 0}/100",
                        style = LocalAppTypography.current.bodyBase,
                        color = LocalAppColorScheme.current.text.default.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroInformationCard(
    label: String,
    showHelpButton: Boolean,
    modifier: Modifier = Modifier,
    helpMessage: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var showHelpTooltip by rememberSaveable { mutableStateOf(false) }
    val shape = RoundedCornerShape(LocalAppRadius.current.value100)
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .clip(shape)
                .background(
                    color = LocalAppColorScheme.current.background.brand.tertiary,
                    shape = shape
                )
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(
                    space = LocalAppSpacing.current.value050,
                    alignment = Alignment.CenterVertically
                ),
            modifier = Modifier.padding(LocalAppSpacing.current.value300)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = LocalAppTypography.current.bodyXSmallStrong,
                    color = LocalAppColorScheme.current.text.brand.onTertiary,
                    modifier = Modifier.weight(1f)
                )

                if (showHelpButton && helpMessage != null) {
                    Box {
                        AppButton(
                            type = ButtonType.Tertiary,
                            size = SizeType.ExtraSmall,
                            onClick = { showHelpTooltip = true },
                            displayLabel = false,
                            displayIcon = true,
                            icon = AppIcons.Help,
                            iconAlt = stringResource(R.string.help_icon_alt)
                        )

                        if (showHelpTooltip) {
                            Tooltip(
                                messages = arrayOf(AnnotatedString(helpMessage)),
                                visible = showHelpTooltip,
                                onDismissRequest = { showHelpTooltip = false }
                            )
                        }
                    }
                }
            }

            content()
        }
    }
}

@Composable
private fun StatusIndicationBar(
    status: AnalysisStatus,
    refreshingError: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(LocalAppRadius.current.value100)
    val isFinished = status != AnalysisStatus.Queued && status != AnalysisStatus.InProgress
    val textResourceId =
        when {
            refreshingError -> R.string.details_screen_refreshing_status_error
            isFinished -> R.string.details_screen_refreshing_status_completed
            else -> R.string.details_screen_refreshing_status_in_progress
        }
    val transition =
        updateTransition(Pair(isFinished, refreshingError), "Analysis details status bar")
    val backgroundColor by
        transition.animateColor(
            transitionSpec = { tween(300) },
            label = "Analysis details status bar background color"
        ) { (finished, refreshingError) ->
            when {
                refreshingError -> LocalAppColorScheme.current.background.warning.tertiary
                finished -> LocalAppColorScheme.current.background.brand.tertiary
                else -> LocalAppColorScheme.current.background.default.primary
            }
        }
    val borderColor by
        transition.animateColor(
            transitionSpec = { tween(300) },
            label = "Analysis details status bar border color"
        ) { (_, refreshingError) ->
            when {
                refreshingError -> LocalAppColorScheme.current.border.warning.primary
                else -> LocalAppColorScheme.current.border.brand.primary
            }
        }
    val foregroundColor by transition.animateColor(
        transitionSpec = { tween(300) },
        label = "Analysis details status bar foreground color"
    ) { (_, refreshingError) ->
        when {
            refreshingError -> LocalAppColorScheme.current.text.warning.primary
            else -> LocalAppColorScheme.current.text.brand.primary
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(space = LocalAppSpacing.current.value200),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .graphicsLayer(
                    shadowElevation = with(LocalDensity.current) { 4.dp.toPx() },
                    shape = shape,
                    clip = false
                ).background(color = backgroundColor, shape = shape)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                ).padding(LocalAppSpacing.current.value300)
    ) {
        Text(
            text = stringResource(textResourceId),
            style = LocalAppTypography.current.bodyBase,
            color = foregroundColor,
            modifier = Modifier.weight(1f)
        )

        AnimatedContent(
            targetState = Pair(isFinished, refreshingError),
            transitionSpec = {
                scaleIn() + fadeIn() togetherWith
                    fadeOut() + scaleOut()
            }
        ) { (finished, refreshingError) ->
            when {
                refreshingError ->
                    Icon(
                        imageVector = AppIcons.CloudOff,
                        contentDescription =
                            stringResource(
                                R.string.details_screen_refreshing_status_error_icon_alt
                            ),
                        tint = foregroundColor,
                        modifier = Modifier.size(24.dp)
                    )
                finished ->
                    Icon(
                        imageVector = AppIcons.Check,
                        contentDescription =
                            stringResource(
                                R.string.details_screen_refreshing_status_completed_icon_alt
                            ),
                        tint = foregroundColor,
                        modifier = Modifier.size(24.dp)
                    )
                else ->
                    CircularProgressIndicator(
                        color = LocalAppColorScheme.current.icon.brand.primary,
                        trackColor = LocalAppColorScheme.current.border.default.primary,
                        modifier = Modifier.size(24.dp)
                    )
            }
        }
    }
}