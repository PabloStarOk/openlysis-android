package com.openlysis.feature.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.openlysis.core.designsystem.components.SectionTitle
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.tools.components.ToolCard
import com.openlysis.feature.tools.components.rememberAnalyzeEmailModalState
import com.openlysis.feature.tools.data.Tool
import com.openlysis.feature.tools.data.ToolCategory

/**
 * Display the analysis tools screen with sections for message and data analysis tools.
 * Users can select different analysis tools like email, SMS, URL, and file analysis.
 *
 * @param onMessageAnalysisStart Callback triggered when message analysis is initiated.
 * @param modifier Optional [Modifier] to apply to the top-level layout container.
 * @param viewModel The view model handling the business logic and data operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ToolsScreen(
    onMessageAnalysisStart: (MessageAnalysis) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ToolsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val modalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var activeCategory by rememberSaveable { mutableStateOf<ToolCategory>(ToolCategory.None) }
    val messageUiNotifier = remember { MessageUiNotifier(context) }
    val errorHandler = remember { AnalysisErrorUiHandler(messageUiNotifier) }

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
        modifier =
            modifier
                .padding(LocalAppSpacing.current.value400)
                .verticalScroll(scrollState)
    ) {
        SectionScaffold(
            title = stringResource(R.string.message_analysis_tools_section_title),
            content = {
                viewModel.toolsRepository.getMessageAnalysisTools().forEach { tool ->
                    MapToolCard(
                        tool = tool,
                        onClick = { activeCategory = tool.category }
                    )
                }
            }
        )

        SectionScaffold(
            title = stringResource(R.string.other_analysis_tools_section_title),
            content = {
                viewModel.toolsRepository.getDataAnalysisTools().forEach { tool ->
                    MapToolCard(
                        tool = tool,
                        onClick = { activeCategory = tool.category }
                    )
                }
            }
        )
    }

    when (activeCategory) {
        ToolCategory.None -> { }
        ToolCategory.Email -> {
            val state =
                rememberAnalyzeEmailModalState(
                    messageUiNotifier = messageUiNotifier,
                    fileAttachmentSettings = viewModel.fileAttachmentSettings
                )
            AnalyzeEmailModal(
                onSubmitClick = { state, files ->
                    viewModel.startEmailAnalysis(
                        messageState = state,
                        attachedFiles = files,
                        onSuccess = {
                            onMessageAnalysisStart(it)
                        },
                        onError = { errorHandler.handle(it) }
                    )
                },
                onDismissRequest = { activeCategory = ToolCategory.None },
                toolState = state,
                modalState = modalState,
                messageUiNotifier = messageUiNotifier
            )
        }
        ToolCategory.Sms -> { }
        ToolCategory.Url -> { }
        ToolCategory.File -> { }
    }
}

/**
 * A scaffold for each section in the screen.
 */
@Composable
private fun SectionScaffold(
    title: String,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
        modifier = modifier
    ) {
        SectionTitle(
            title = title,
            modifier = Modifier.fillMaxWidth()
        )

        content()
    }
}

/**
 * Creates a UI Card from the specified [Tool].
 */
@Composable
private fun MapToolCard(
    tool: Tool,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToolCard(
        label = stringResource(tool.nameResource),
        description = stringResource(tool.descriptionResource),
        icon = ImageVector.vectorResource(tool.iconResource),
        iconAlt = stringResource(tool.iconAltResource),
        onClick = onClick,
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ToolsScreenPreview() {
    OpenlysisTheme(darkTheme = false) {
        ToolsScreen(
            onMessageAnalysisStart = { }
        )
    }
}