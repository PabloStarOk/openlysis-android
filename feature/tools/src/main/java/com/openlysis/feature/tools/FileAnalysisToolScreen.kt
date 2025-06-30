package com.openlysis.feature.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.feature.tools.components.AttachFilesSection
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.data.AttachedFileData

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
    val errorHandler = remember { AnalysisErrorUiHandler(messageUiNotifier) }
    val fileAttachmentSettings = viewModel.fileAttachmentSettings.copy(maxFilesAmount = 1)
    val attachedFiles =
        rememberSaveable(
            saver =
                listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
        ) {
            mutableStateListOf<AttachedFileData>()
        }
    val isFileAttached by remember(attachedFiles) {
        derivedStateOf {
            attachedFiles.isNotEmpty()
        }
    }

    ToolScreenScaffold(
        onSubmitClick = {
            viewModel.startFileAnalysis(
                attachedFile = attachedFiles.first(),
                onSuccess = onAnalysisStart,
                onError = errorHandler::handle
            )
        },
        screenTitle = stringResource(R.string.file_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitEnabled = isFileAttached,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        modifier = modifier
    ) {
        AttachFilesSection(
            attachedFiles = attachedFiles,
            onFileAttach = { attachedFiles.add(it) },
            onFileDetach = { attachedFiles.removeAt(0) },
            onFilePasswordChange = { _, password ->
                attachedFiles[0] = attachedFiles[0].copy(password = password)
            },
            enabled = !isFileAttached,
            title = stringResource(R.string.file_tool_section_title),
            description = stringResource(R.string.file_tool_section_description),
            settings = fileAttachmentSettings
        )
    }
}