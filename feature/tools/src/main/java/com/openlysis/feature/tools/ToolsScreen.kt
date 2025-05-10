package com.openlysis.feature.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.ui.components.SectionTitle
import com.openlysis.core.ui.theme.OpenlysisTheme
import com.openlysis.core.ui.theme.size.Spacing
import com.openlysis.feature.tools.components.ToolCard
import com.openlysis.feature.tools.data.Tool
import com.openlysis.feature.tools.data.ToolsDataSource
import com.openlysis.feature.tools.data.ToolsRepository

/**
 * Composable function that displays the tools screen.
 *
 * This screen presents a list of different tool categories, each containing
 * specific tools for message analysis, other data analysis, and reputation checking.
 * The layout uses a vertical column with spacing between sections and is scrollable.
 *
 * @param repository The [ToolsRepository] providing the data for the various tool sections.
 * @param modifier Optional [Modifier] to apply to the top-level layout container.
 */
@Composable
internal fun ToolsScreen(
    repository: ToolsRepository,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Value800),
        modifier =
            modifier
                .padding(Spacing.Value400)
                .verticalScroll(rememberScrollState())
    ) {
        MessageToolsSection(repository.getMessageAnalysisTools())
        OtherAnalysesSection(repository.getDataAnalysisTools())
        ReputationToolsSection(repository.getReputationTools())
    }
}

@Composable
private fun SectionScaffold(
    title: String,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Value400),
        modifier = modifier
    ) {
        SectionTitle(
            title = title,
            modifier = Modifier.fillMaxWidth()
        )

        content()
    }
}

@Composable
private fun MapToolCard(
    tool: Tool,
    modifier: Modifier = Modifier
) {
    ToolCard(
        label = stringResource(tool.nameResource),
        description = stringResource(tool.descriptionResource),
        icon = ImageVector.vectorResource(tool.iconResource),
        iconAlt = stringResource(tool.iconAltResource),
        onClick = tool.onClick,
        modifier = modifier
    )
}

@Composable
private fun MessageToolsSection(
    tools: List<Tool>,
    modifier: Modifier = Modifier
) {
    SectionScaffold(
        title = stringResource(R.string.message_analysis_tools_section_title),
        content = {
            tools.forEach { tool -> MapToolCard(tool) }
        },
        modifier = modifier
    )
}

@Composable
private fun OtherAnalysesSection(
    tools: List<Tool>,
    modifier: Modifier = Modifier
) {
    SectionScaffold(
        title = stringResource(R.string.other_analysis_tools_section_title),
        content = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.Value400)
            ) {
                tools.forEach { tool ->
                    MapToolCard(
                        tool,
                        Modifier.weight(1f)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
private fun ReputationToolsSection(
    tools: List<Tool>,
    modifier: Modifier = Modifier
) {
    SectionScaffold(
        title = stringResource(R.string.reputation_tools_section_title),
        content = {
            tools.forEach { tool -> MapToolCard(tool) }
        },
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