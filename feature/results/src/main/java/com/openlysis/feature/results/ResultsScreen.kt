package com.openlysis.feature.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.feature.results.components.initial.AnalysisResultsCard

/**
 * Screen to access the analysis results of the user.
 *
 * @param viewModel The view model that manages the screen's state and data
 * @param onEmailResultsClick Callback invoked when the email results card is clicked
 * @param onSmsResultsClick Callback invoked when the SMS results card is clicked
 * @param onFileResultsClick Callback invoked when the file results card is clicked
 * @param onUrlResultsClick Callback invoked when the URL results card is clicked
 * @param modifier Optional modifier for customizing the screen's layout
 */
@Composable
internal fun ResultsScreen(
    viewModel: ResultsScreenViewModel,
    onEmailResultsClick: () -> Unit,
    onSmsResultsClick: () -> Unit,
    onFileResultsClick: () -> Unit,
    onUrlResultsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    if (!initialized) {
        LaunchedEffect(Unit) {
            initialized = true
            viewModel.loadStats()
        }
    }

    val verticalScroll = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
        modifier =
            modifier
                .padding(LocalAppSpacing.current.value400)
                .verticalScroll(verticalScroll)
    ) {
        AnalysisResultsCard(
            onClick = onEmailResultsClick,
            title = stringResource(R.string.results_screen_emails_card_title),
            description = stringResource(R.string.results_screen_emails_card_description),
            verdictStatsState = uiState.emailAnalysesStats
        )

        AnalysisResultsCard(
            onClick = onSmsResultsClick,
            title = stringResource(R.string.results_screen_sms_card_title),
            description = stringResource(R.string.results_screen_sms_card_description),
            verdictStatsState = uiState.smsAnalysesStats
        )

        AnalysisResultsCard(
            onClick = onFileResultsClick,
            title = stringResource(R.string.results_screen_files_card_title),
            description = stringResource(R.string.results_screen_files_card_description),
            verdictStatsState = uiState.fileAnalysesStats
        )

        AnalysisResultsCard(
            onClick = onUrlResultsClick,
            title = stringResource(R.string.results_screen_urls_card_title),
            description = stringResource(R.string.results_screen_urls_card_description),
            verdictStatsState = uiState.urlAnalysesStats
        )
    }
}