package com.openlysis.feature.tools

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import java.net.URI

/**
 * Composable screen for URL analysis that allows users to input and analyze URLs.
 *
 * @param viewModel The view model that handles business logic and data operations
 * @param onAnalysisStart Callback invoked when URL analysis starts, receiving the [UrlMultiAnalysis] result
 * @param onTopBarUpdate Callback to update the top bar state of the screen
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
internal fun UrlAnalysisToolScreen(
    viewModel: ToolsScreenViewModel,
    onAnalysisStart: (UrlMultiAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    var urlValue by rememberSaveable { mutableStateOf("") }
    var isValidUrl by
        rememberSaveable {
            mutableStateOf(true)
        }
    val submitEnabled by
        remember(urlValue) {
            derivedStateOf {
                urlValue.isNotBlank()
            }
        }
    val analysisRequestState = viewModel.currentAnalysisRequest.collectAsStateWithLifecycle()

    ToolScreenScaffold(
        onSubmitRequest = {
            val url = viewModel.getUrlIfValid(urlValue)
            viewModel.startUrlAnalysis(url = url as URI)
        },
        canSubmit = {
            isValidUrl = viewModel.getUrlIfValid(urlValue) != null
            isValidUrl
        },
        onGoToAnalysisRequest = {
            if (analysisRequestState.value is AnalysisRequestState.Success.Url) {
                val value = analysisRequestState.value as AnalysisRequestState.Success.Url
                onAnalysisStart(value.initialValue)
            }
        },
        onCancelRequest = viewModel::cancelCurrentRequest,
        analysisRequestState = analysisRequestState.value,
        screenTitle = stringResource(R.string.url_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitEnabled = submitEnabled,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        modifier = modifier
    ) { requestSubmit ->
        ToolSection(
            title = stringResource(R.string.url_tool_section_title),
            description = stringResource(R.string.url_tool_section_description)
        ) {
            TextInput(
                value = urlValue,
                onValueChange = { urlValue = it },
                label = stringResource(R.string.url_tool_input_label),
                isError = !isValidUrl,
                supportingText = {
                    if (isValidUrl) {
                        return@TextInput
                    }

                    Text(
                        text = stringResource(R.string.url_tool_input_invalid_url),
                        style = LocalAppTypography.current.bodySmall,
                        color = LocalAppColorScheme.current.text.danger.secondary,
                        modifier = Modifier.padding(horizontal = LocalAppSpacing.current.value300)
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Send
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSend = {
                            if (submitEnabled) {
                                requestSubmit()
                            }
                        }
                    )
            )
        }
    }
}