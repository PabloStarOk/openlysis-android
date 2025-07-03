package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.components.rememberAttachFilesState
import com.openlysis.feature.tools.data.AnalysisRequestState

/**
 * Composable screen for file analysis tool.
 *
 * @param viewModel The view model handling the business logic for the tools screen
 * @param onAnalysisStart Callback invoked when file analysis starts, provides the initial [FileMultiAnalysis] result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Modifier for customizing the layout. Defaults to [Modifier].
 */
@Composable
internal fun FileAnalysisToolScreen(
    viewModel: ToolsScreenViewModel,
    onAnalysisStart: (FileMultiAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messageUiNotifier = remember { MessageUiNotifier(context) }
    val fileAttachmentSettings = viewModel.fileAttachmentSettings.copy(maxFilesAmount = 1)
    val attachFilesState = rememberAttachFilesState(fileAttachmentSettings, messageUiNotifier)
    val isFileAttached by remember(attachFilesState) {
        derivedStateOf {
            attachFilesState.attachedFiles.isNotEmpty()
        }
    }
    val analysisRequestState = viewModel.currentAnalysisRequest.collectAsStateWithLifecycle()

    ToolScreenScaffold(
        onSubmitRequest = {
            viewModel.startFileAnalysis(attachedFile = attachFilesState.attachedFiles.first())
        },
        onGoToAnalysisRequest = {
            if (analysisRequestState.value is AnalysisRequestState.Success.File) {
                val value = analysisRequestState.value as AnalysisRequestState.Success.File
                onAnalysisStart(value.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelCurrentRequest,
        analysisRequestState = analysisRequestState,
        screenTitle = stringResource(R.string.file_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitEnabled = isFileAttached,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        modifier = modifier
    ) {
        AttachFilesSection(
            attachedFiles = attachFilesState.attachedFiles,
            onFileAttach = attachFilesState::attachFile,
            onFileDetach = attachFilesState::detachFile,
            onFilePasswordChange = attachFilesState::updateFilePassword,
            enabled = !isFileAttached,
            title = stringResource(R.string.file_tool_section_title),
            description = stringResource(R.string.file_tool_section_description),
            settings = fileAttachmentSettings
        )
    }
}