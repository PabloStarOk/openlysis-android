package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.model.AnalysisRequestState
import com.openlysis.feature.tools.model.AttachedFileData

/**
 * Composable screen for file analysis tool.
 *
 * @param viewModel The view model handling the business logic for the screen
 * @param onAnalysisStart Callback invoked when file analysis starts, provides the initial [FileMultiAnalysis] result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Modifier for customizing the layout. Defaults to [Modifier].
 */
@Composable
internal fun FileAnalysisToolScreen(
    viewModel: FileAnalysisToolScreenViewModel,
    onAnalysisStart: (FileMultiAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val requestState = uiState.requestState

    ToolScreenScaffold(
        onSubmitRequest = viewModel::startAnalysis,
        onGoToAnalysisRequest = {
            if (requestState is AnalysisRequestState.Success.File) {
                onAnalysisStart(requestState.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelRequest,
        analysisRequestState = requestState,
        screenTitle = stringResource(R.string.file_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitEnabled = uiState.canRequestAnalysis,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        modifier = modifier
    ) {
        AttachFilesSection(
            attachedFiles =
                if (uiState.isFileAttached) {
                    listOf(uiState.file as AttachedFileData)
                } else {
                    emptyList()
                },
            onFileAttach = viewModel::addFile,
            onFileDetach = { viewModel.removeFile() },
            onFilePasswordChange = { _, passwd -> viewModel.updateFilePassword(passwd) },
            enabled = !uiState.isFileAttached,
            title = stringResource(R.string.file_tool_section_title),
            description = stringResource(R.string.file_tool_section_description),
            settings = viewModel.attachmentSettings
        )
    }
}