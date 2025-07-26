package com.openlysis.feature.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.feature.results.components.VerdictStats
import com.openlysis.feature.results.components.preview.AnalysisPreview
import com.openlysis.feature.results.components.preview.FiltersDialog
import com.openlysis.feature.results.components.preview.RefreshButton
import com.openlysis.feature.results.util.getNetworkErrorMessage
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**
 * Screen to display the previews of analysis results.
 *
 * @param TResult The type of model being displayed, must extend [Model]
 * @param viewModel The view model handling the business logic and state management
 * @param onTopBarUpdate Callback to update the top bar state
 * @param screenTitle The title to be displayed in the top bar
 * @param previewCardHeaderLabel The label to be displayed in the header of each analysis preview.
 * @param modifier Optional modifier for the composable layout
 */
@Composable
internal fun <TResult : Model> PreviewsScreen(
    viewModel: PreviewsScreenViewModel<TResult>,
    onTopBarUpdate: (TopBarState) -> Unit,
    onPreviewDetailsClick: (String) -> Unit,
    screenTitle: String,
    previewCardHeaderLabel: String,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onTopBarUpdate(TopBarState(title = screenTitle))
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val showLoadingIndicator by
        remember(uiState.loadingState) {
            derivedStateOf {
                uiState.loadingState is LoadingState.InProgress
            }
        }
    val showStatusMessage by
        remember(uiState.loadingState, uiState.canLoadMore) {
            derivedStateOf {
                uiState.loadingState is LoadingState.Error ||
                    !uiState.canLoadMore ||
                    (
                        uiState.loadingState !is LoadingState.InProgress &&
                            uiState.previews.isEmpty()
                    )
            }
        }
    val shouldLoadMore by
        remember(uiState.canLoadMore, uiState.previews, uiState.loadingState) {
            derivedStateOf {
                if (!uiState.canLoadMore || uiState.loadingState !is LoadingState.Idle) {
                    return@derivedStateOf false
                }

                val totalAnalyses = uiState.previews.size
                val lastVisibleIndex =
                    gridState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: 0
                val remainingAnalyses = totalAnalyses - lastVisibleIndex
                remainingAnalyses <= 2
            }
        }
    val showRefreshAllButton by
        remember(uiState.previews) {
            derivedStateOf {
                uiState.previews.any {
                    it.status == AnalysisStatus.Queued ||
                        it.status == AnalysisStatus.InProgress
                }
            }
        }
    var isRefreshingPreviews by rememberSaveable { mutableStateOf(false) }
    var showFiltersDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadPreviews()
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 350.dp),
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
        horizontalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value800,
                alignment = Alignment.CenterHorizontally
            ),
        contentPadding = PaddingValues(LocalAppSpacing.current.value400),
        modifier = modifier
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                VerdictStats(
                    state = uiState.verdictStats,
                    label = stringResource(R.string.verdict_stats_last_results_label),
                    smallSize = true,
                    showUnknown = true,
                    modifier = Modifier.width(IntrinsicSize.Min)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
                    modifier = modifier
                ) {
                    AppButton(
                        type = ButtonType.Secondary,
                        size = SizeType.Default,
                        onClick = { showFiltersDialog = true },
                        displayLabel = false,
                        displayIcon = true,
                        icon = AppIcons.Filter,
                        iconAlt = stringResource(R.string.previews_screen_filter_button_icon_alt)
                    )

                    if (showRefreshAllButton) {
                        RefreshButton(
                            onRefreshClick = {
                                isRefreshingPreviews = true
                                viewModel.refreshAllPreviews(
                                    onFinished = {
                                        delay(1.seconds)
                                        isRefreshingPreviews = false
                                    }
                                )
                            },
                            isRefreshing = isRefreshingPreviews,
                            displayLabel = false
                        )
                    }
                }
            }
        }

        items(
            items = uiState.previews,
            key = { preview -> preview.id }
        ) { preview ->
            var isRefreshing by remember { mutableStateOf(false) }
            AnalysisPreview(
                onDetailsClick = { onPreviewDetailsClick(preview.id) },
                onRefreshClick = {
                    isRefreshing = true
                    viewModel.refreshPreview(
                        id = preview.id,
                        onFinished = {
                            delay(1.seconds)
                            isRefreshing = false
                        }
                    )
                },
                headerLabel = previewCardHeaderLabel,
                state = preview,
                isRefreshing = isRefreshing
            )
        }

        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            AnimatedVisibility(
                visible = showLoadingIndicator
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LocalAppColorScheme.current.icon.brand.primary,
                        trackColor = LocalAppColorScheme.current.border.default.primary,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = showStatusMessage,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                StatusMessage(
                    loadingState = uiState.loadingState,
                    noPreviews = uiState.previews.isEmpty(),
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showFiltersDialog) {
        FiltersDialog(
            currentFilters = uiState.filtersState,
            defaultFilters = viewModel.defaultFiltersState,
            onApplyRequest = { newFilters ->
                viewModel.updateFilters(newFilters)
                showFiltersDialog = false
            },
            onDismissRequest = { showFiltersDialog = false }
        )
    }
}

@Composable
private fun StatusMessage(
    loadingState: LoadingState,
    noPreviews: Boolean,
    modifier: Modifier = Modifier
) {
    val appColorScheme = LocalAppColorScheme.current
    val textColor =
        if (loadingState is LoadingState.Error) {
            appColorScheme.text.danger.secondary
        } else {
            appColorScheme.text.default.tertiary
        }

    val message =
        if (loadingState is LoadingState.Error) {
            getNetworkErrorMessage(loadingState.error)
        } else if (noPreviews) {
            stringResource(R.string.analyses_limit_reached_no_analyses)
        } else {
            stringResource(R.string.analyses_limit_reached_no_more_analyses)
        }

    Text(
        text = message,
        style = LocalAppTypography.current.bodyBase,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}