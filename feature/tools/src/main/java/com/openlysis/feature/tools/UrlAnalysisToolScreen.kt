package com.openlysis.feature.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.components.ToolSection
import com.openlysis.feature.tools.data.AnalysisRequestState

/**
 * Composable screen for URL analysis that allows users to input and analyze URLs.
 *
 * @param viewModel The view model that handles business logic of the screen
 * @param onAnalysisStart Callback invoked when URL analysis starts, receiving the [UrlMultiAnalysis] result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
internal fun UrlAnalysisToolScreen(
    viewModel: UrlAnalysisToolScreenViewModel,
    onAnalysisStart: (UrlMultiAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val requestState = uiState.requestState
    val showInputError = !uiState.isValidUrl && uiState.url.isNotEmpty()

    ToolScreenScaffold(
        onSubmitRequest = viewModel::startAnalysis,
        onGoToAnalysisRequest = {
            if (requestState is AnalysisRequestState.Success.Url) {
                onAnalysisStart(requestState.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelRequest,
        analysisRequestState = requestState,
        screenTitle = stringResource(R.string.url_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitEnabled = uiState.canRequestAnalysis,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        modifier = modifier
    ) {
        ToolSection(
            title = stringResource(R.string.url_tool_section_title),
            description = stringResource(R.string.url_tool_section_description)
        ) {
            TextInput(
                value = uiState.url,
                onValueChange = viewModel::updateUrl,
                label = stringResource(R.string.url_tool_input_label),
                isError = showInputError,
                supportingText = {
                    AnimatedVisibility(
                        visible = showInputError,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Text(
                            text = stringResource(R.string.url_tool_input_invalid_url),
                            style = LocalAppTypography.current.bodySmall,
                            color = LocalAppColorScheme.current.text.danger.secondary,
                            modifier =
                                Modifier.padding(
                                    horizontal = LocalAppSpacing.current.value300
                                )
                        )
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Send
                    ),
                keyboardActions = KeyboardActions(onSend = { viewModel.startAnalysis() })
            )
        }
    }
}