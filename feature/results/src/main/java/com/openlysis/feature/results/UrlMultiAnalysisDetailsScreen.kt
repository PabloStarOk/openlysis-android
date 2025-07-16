package com.openlysis.feature.results

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.feature.results.components.detail.DetailsScreenScaffold
import com.openlysis.feature.results.components.detail.DetailsScreenScaffoldData
import com.openlysis.feature.results.components.detail.HashValuesSection
import com.openlysis.feature.results.components.detail.ServiceResultData
import com.openlysis.feature.results.components.detail.ServiceResultsSection

/**
 * Details screen for a URL multi-analysis.
 *
 * @param viewModel The ViewModel providing the UI state for the URL multi-analysis analysis.
 * @param onTopBarUpdate Callback to update the top bar state.
 * @param analysisId The ID of the analysis to load and display.
 * @param modifier Modifier for styling the composable.
 */
@Composable
internal fun UrlMultiAnalysisDetailsScreen(
    viewModel: DetailsScreenViewModel<UrlMultiAnalysis>,
    onTopBarUpdate: (TopBarState) -> Unit,
    analysisId: String,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPolling by viewModel.isPolling.collectAsStateWithLifecycle()

    val analysis =
        if (uiState is DetailsUiState.Success) {
            val success = uiState as DetailsUiState.Success<UrlMultiAnalysis>
            success.analysis
        } else {
            null
        }

    val scaffoldData =
        analysis?.let {
            DetailsScreenScaffoldData(
                heroInfoCardData =
                    Pair(stringResource(R.string.details_screen_url_label), it.url.toString()),
                status = analysis.status,
                verdict = analysis.finalVerdict,
                threatScore = analysis.avgThreatScore,
                startedDate = analysis.startedDate,
                informationSectionTitle = null,
                informationSectionItems = null
            )
        }

    DetailsScreenScaffold(
        onTopBarUpdate = onTopBarUpdate,
        onLoadDetails = { viewModel.loadAnalysis(analysisId) },
        onPollingStart = { viewModel.startPolling(analysisId) },
        onPollingStop = viewModel::stopPolling,
        screenTitle = stringResource(R.string.details_screen_url_title),
        uiState = uiState,
        isPolling = isPolling,
        data = scaffoldData,
        modifier = modifier
    ) { analysis ->
        ServiceResultsSection(
            isPrimarySection = true,
            serviceResults =
                analysis.analyses.map {
                    ServiceResultData(
                        serviceName = it.serviceName,
                        status = it.status,
                        verdict = it.verdict,
                        threatScore = it.threatScore
                    )
                },
            initiallyExpanded = true
        )

        HashValuesSection(
            isPrimarySection = true,
            hashValues = analysis.hashValues,
            initiallyExpanded = true
        )
    }
}