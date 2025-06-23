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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.SectionTitle
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.feature.tools.components.ToolCard
import com.openlysis.feature.tools.data.Tool
import com.openlysis.feature.tools.data.ToolsDataSource
import com.openlysis.feature.tools.data.ToolsRepository

/**
 * Display the analysis tools screen.
 *
 * @param repository The [ToolsRepository] providing the data for the various tool sections.
 * @param modifier Optional [Modifier] to apply to the top-level layout container.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ToolsScreen(
    repository: ToolsRepository,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val modalState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    var showEmailToolModal by remember { mutableStateOf(false) }

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
                repository.getMessageAnalysisTools().forEach { tool ->
                    MapToolCard(
                        tool = tool,
                        onClick = {
                            showEmailToolModal = true
                        }
                    )
                }
            }
        )

        SectionScaffold(
            title = stringResource(R.string.other_analysis_tools_section_title),
            content = {
                repository.getDataAnalysisTools().forEach { tool ->
                    MapToolCard(
                        tool = tool,
                        onClick = { }
                    )
                }
            }
        )
    }

    if (showEmailToolModal) {
        AnalyzeEmailModal(
            onDismissRequest = { showEmailToolModal = false },
            state = modalState
        )
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
        ToolsScreen(ToolsDataSource())
    }
}