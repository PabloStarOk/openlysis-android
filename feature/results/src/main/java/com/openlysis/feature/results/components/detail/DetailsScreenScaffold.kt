package com.openlysis.feature.results.components.detail

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.DetailsUiState
import com.openlysis.feature.results.R
import com.openlysis.feature.results.components.AnalysisStatusBadge
import com.openlysis.feature.results.components.AnalysisVerdictBadge
import com.openlysis.feature.results.util.getRepositoryErrorMessage
import kotlinx.coroutines.delay
import kotlinx.datetime.toKotlinInstant

/**
 * Scaffold for displaying details screens.
 *
 * @param onTopBarUpdate Callback to update the top bar state
 * @param onLoadDetails Callback to load the result details to display
 * @param onPollingStart Callback to invoke when polling should start
 * @param onPollingStop Callback to invoke when polling should stop
 * @param screenTitle Title to be displayed in the top bar
 * @param uiState Current UI state of the details screen
 * @param isPolling Indicates if polling is active
 * @param data Data to be displayed in the scaffold
 * @param modifier Optional modifier for customizing the layout
 * @param content Custom content to be displayed within the scaffold
 */
@Composable
internal fun DetailsScreenScaffold(
    onTopBarUpdate: (TopBarState) -> Unit,
    onLoadDetails: () -> Unit,
    onPollingStart: () -> Unit,
    onPollingStop: () -> Unit,
    screenTitle: String,
    uiState: DetailsUiState<*>,
    isPolling: Boolean,
    data: DetailsScreenScaffoldData?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    if (!initialized) {
        initialized = true
        LaunchedEffect(Unit) {
            onTopBarUpdate(TopBarState(title = screenTitle))
            onLoadDetails()
        }
    }

    DisposableEffect(Unit) {
        onPollingStart()
        onDispose {
            onPollingStop()
        }
    }

    val scrollState = rememberScrollState()
    val shouldShowPollingBar by remember(uiState, isPolling, data) {
        derivedStateOf {
            uiState is DetailsUiState.Success &&
                isPolling &&
                data?.status == AnalysisStatus.Queued ||
                data?.status == AnalysisStatus.InProgress
        }
    }
    var isPollingVarVisible by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(shouldShowPollingBar) {
        if (shouldShowPollingBar) {
            isPollingVarVisible = true
        } else {
            delay(5000)
            isPollingVarVisible = false
        }
    }

    Column {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                modifier
                    .verticalScroll(scrollState)
                    .padding(LocalAppSpacing.current.value400)
                    .weight(1f)
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
                    text = getRepositoryErrorMessage(error),
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
                threatScore = null,
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

            content()
        }

        if (data == null) return@Column

        AnimatedVisibility(
            visible = isPollingVarVisible,
            enter = expandVertically() + slideInVertically(initialOffsetY = { -it }),
            exit = shrinkVertically() + slideOutVertically(targetOffsetY = { it })
        ) {
            PollingIndicationBar(
                status = data.status,
                modifier = Modifier.fillMaxWidth()
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
private fun PollingIndicationBar(
    status: AnalysisStatus,
    modifier: Modifier = Modifier
) {
    val isFinished = status != AnalysisStatus.Queued && status != AnalysisStatus.InProgress
    val textResourceId =
        if (isFinished) {
            R.string.details_screen_refreshing_status_completed
        } else {
            R.string.details_screen_refreshing_status_in_progress
        }

    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = LocalAppColorScheme.current.border.default.primary
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    space = LocalAppSpacing.current.value200,
                    alignment = Alignment.CenterHorizontally
                ),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = LocalAppColorScheme.current.background.default.primary
                    ).padding(LocalAppSpacing.current.value300)
        ) {
            Text(
                text = stringResource(textResourceId),
                style = LocalAppTypography.current.bodyBase,
                color = LocalAppColorScheme.current.text.brand.primary
            )

            AnimatedContent(
                targetState = isFinished,
                transitionSpec = {
                    scaleIn() + fadeIn() togetherWith
                        fadeOut() + scaleOut()
                }
            ) { targetState ->
                if (targetState) {
                    Icon(
                        imageVector = AppIcons.Check,
                        contentDescription =
                            stringResource(
                                R.string.details_screen_refreshing_status_completed_icon_alt
                            ),
                        tint = LocalAppColorScheme.current.icon.positive.primary,
                        modifier = Modifier.size(25.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        color = LocalAppColorScheme.current.icon.brand.primary,
                        trackColor = LocalAppColorScheme.current.border.default.primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}