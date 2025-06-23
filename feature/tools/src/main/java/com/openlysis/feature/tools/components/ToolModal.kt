package com.openlysis.feature.tools.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing

/**
 * Modal bottom sheet for tool actions, with scrollable content and a submit button.
 *
 * @param onSubmitClick Callback when the submit button is clicked.
 * @param onDismissRequest Callback when the modal should be dismissed.
 * @param state The state of the bottom sheet.
 * @param submitButtonLabel The label for the submit button.
 * @param submitButtonIconAlt The content description for the submit button icon.
 * @param modifier Modifier for styling.
 * @param content The content to display inside the modal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ToolModal(
    onSubmitClick: () -> Unit,
    onDismissRequest: () -> Unit,
    state: SheetState,
    submitButtonLabel: String,
    submitButtonIconAlt: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = state,
        shape =
            RoundedCornerShape(
                topStart = LocalAppRadius.current.value400,
                topEnd = LocalAppRadius.current.value400
            ),
        containerColor = LocalAppColorScheme.current.background.default.primary,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 108.dp,
                color = LocalAppColorScheme.current.icon.default.primary
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.heightIn(max = 650.dp)
        ) {
            // Content
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
                modifier =
                    Modifier
                        .verticalScroll(state = scrollState)
                        .padding(
                            vertical = LocalAppSpacing.current.value200,
                            horizontal = LocalAppSpacing.current.value800
                        ).weight(1f)
            ) {
                content()
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = LocalAppColorScheme.current.border.default.primary
            )

            // Action button
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
                    type = ButtonType.Primary,
                    size = SizeType.Default,
                    onClick = onSubmitClick,
                    displayLabel = true,
                    label = submitButtonLabel,
                    displayIcon = true,
                    icon = AppIcons.Tools,
                    iconAlt = submitButtonIconAlt,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = false)
@Composable
private fun ToolModalPreview() {
    OpenlysisTheme(darkTheme = false) {
        ToolModal(
            onSubmitClick = { },
            onDismissRequest = { },
            submitButtonLabel = "Test",
            submitButtonIconAlt = "Test",
            state =
                SheetState(
                    skipPartiallyExpanded = true,
                    initialValue = SheetValue.Expanded,
                    density = Density(LocalDensity.current.density)
                )
        ) {
        }
    }
}