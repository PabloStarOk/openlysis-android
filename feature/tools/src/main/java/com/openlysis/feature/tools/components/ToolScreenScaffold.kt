package com.openlysis.feature.tools.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing

/**
 * A scaffold for tool screens. This composable provides a consistent layout structure for tool-related screens
 * with a scrollable content area and a bottom submit button.
 *
 * @param screenTitle The title to display in the top bar.
 * @param onTopBarUpdate Callback to update the top bar state.
 * @param onSubmitClick Callback when the submit button is clicked.
 * @param submitButtonLabel The label for the submit button.
 * @param submitButtonIconAlt The content description for the submit button icon.
 * @param submitEnabled Controls whether the submit button is enabled or disabled.
 * @param modifier Modifier for styling (defaults to [Modifier]).
 * @param content The content to display inside the scrollable area of the scaffold.
 */
@Composable
internal fun ToolScreenScaffold(
    screenTitle: String,
    onTopBarUpdate: (TopBarState) -> Unit,
    onSubmitClick: () -> Unit,
    submitButtonLabel: String,
    submitButtonIconAlt: String,
    submitEnabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    LaunchedEffect(Unit) {
        onTopBarUpdate(
            TopBarState(
                title = screenTitle,
                hasMenu = false
            )
        )
    }

    val scrollState = rememberScrollState()
    val submitButtonType =
        if (submitEnabled) {
            ButtonType.Primary
        } else {
            ButtonType.PrimaryDisabled
        }
    Column {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
            modifier =
                Modifier
                    .animateContentSize()
                    .verticalScroll(state = scrollState)
                    .padding(
                        vertical = LocalAppSpacing.current.value400,
                        horizontal = LocalAppSpacing.current.value600
                    ).weight(1f)
        ) {
            content()
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = LocalAppColorScheme.current.border.default.primary
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = LocalAppSpacing.current.value200,
                        horizontal = LocalAppSpacing.current.value800
                    )
        ) {
            AppButton(
                type = submitButtonType,
                size = SizeType.Default,
                onClick = onSubmitClick,
                displayLabel = true,
                label = submitButtonLabel,
                displayIcon = true,
                icon = AppIcons.Search,
                iconAlt = submitButtonIconAlt,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun ToolScreenScaffoldPreview() {
    OpenlysisTheme(darkTheme = false) {
        ToolScreenScaffold(
            screenTitle = "Test Title",
            onTopBarUpdate = { },
            onSubmitClick = { },
            submitButtonLabel = "Test",
            submitButtonIconAlt = "Test",
            submitEnabled = true
        ) {
        }
    }
}