package com.openlysis.feature.results

import android.text.format.Formatter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.feature.results.components.detail.DetailsScreenScaffold
import com.openlysis.feature.results.components.detail.DetailsScreenScaffoldData
import com.openlysis.feature.results.components.detail.HashValuesSection
import com.openlysis.feature.results.components.detail.ServiceResultData
import com.openlysis.feature.results.components.detail.ServiceResultsSection

/**
 * Details screen for a file multi-analysis.
 *
 * @param viewModel The ViewModel providing the UI state for the file multi-analysis analysis.
 * @param onTopBarUpdate Callback to update the top bar state.
 * @param analysisId The ID of the analysis to load and display.
 * @param modifier Modifier for styling the composable.
 */
@Composable
internal fun FileMultiAnalysisDetailsScreen(
    viewModel: DetailsScreenViewModel<FileMultiAnalysis>,
    onTopBarUpdate: (TopBarState) -> Unit,
    analysisId: String,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val analysis =
        if (uiState is DetailsUiState.Success) {
            val success = uiState as DetailsUiState.Success<FileMultiAnalysis>
            success.analysis
        } else {
            null
        }

    val scaffoldData =
        analysis?.let {
            val formattedFileSize =
                Formatter.formatFileSize(
                    LocalContext.current,
                    analysis.fileMetadata.size
                )
            DetailsScreenScaffoldData(
                heroInfoCardData =
                    Pair(
                        stringResource(R.string.details_screen_file_name_label),
                        it.fileMetadata.name
                    ),
                status = analysis.status,
                verdict = analysis.finalVerdict,
                threatScore = analysis.avgThreatScore,
                startedDate = analysis.startedDate,
                informationSectionTitle =
                    stringResource(R.string.details_screen_file_information_section_title),
                informationSectionItems =
                    buildList {
                        add(
                            Pair(
                                stringResource(R.string.details_screen_file_information_type_label),
                                analysis.fileMetadata.contentType
                            )
                        )
                        add(
                            Pair(
                                stringResource(R.string.details_screen_file_information_size_label),
                                formattedFileSize
                            )
                        )
                    }
            )
        }

    DetailsScreenScaffold(
        onTopBarUpdate = onTopBarUpdate,
        onLoadDetails = { viewModel.loadAnalysis(analysisId) },
        screenTitle = stringResource(R.string.details_screen_file_title),
        uiState = uiState,
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