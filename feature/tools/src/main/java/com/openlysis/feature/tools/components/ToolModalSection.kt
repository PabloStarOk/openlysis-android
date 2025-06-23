package com.openlysis.feature.tools.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * Section for a titled and described area in a [ToolModal], with custom content.
 *
 * @param title The title of the section.
 * @param description The description of the section.
 * @param modifier Modifier for styling.
 * @param content The content to display inside the section.
 */
@Composable
internal fun ToolModalSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value100)
        ) {
            Text(
                text = title,
                style = LocalAppTypography.current.title6,
                color = LocalAppColorScheme.current.text.brand.primary
            )
            Text(
                text = description,
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.default.secondary
            )
        }

        content()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ToolModalSectionPreview() {
    OpenlysisTheme(darkTheme = false) {
        ToolModalSection(
            title = "Example Title",
            description = "This is an description."
        ) {
        }
    }
}