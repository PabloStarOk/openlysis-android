package com.openlysis.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.openlysis.core.designsystem.components.SectionTitle
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing

/**
 * Displays a section that encapsulates related settings.
 *
 * @param title The title of the section.
 * @param modifier Modifier to be applied to the section container.
 * @param content The composable content to be displayed within the section.
 */
@Composable
internal fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
        modifier = modifier
    ) {
        SectionTitle(title)

        Column(
            content = content,
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
            modifier =
                Modifier
                    .padding(horizontal = LocalAppSpacing.current.value300)
                    .fillMaxWidth()
        )
    }
}