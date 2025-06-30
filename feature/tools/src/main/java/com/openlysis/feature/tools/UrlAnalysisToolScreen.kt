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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.feature.tools.components.ToolScreenScaffold
import com.openlysis.feature.tools.components.ToolSection

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
    val context = LocalContext.current
    val messageUiNotifier = remember { MessageUiNotifier(context) }
    val errorHandler = remember { AnalysisErrorUiHandler(messageUiNotifier) }
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

    fun onSubmit() {
        val url = viewModel.getUrlIfValid(urlValue)
        isValidUrl = url != null
        if (url == null) {
            return
        }

        viewModel.startUrlAnalysis(
            url = url,
            onSuccess = onAnalysisStart,
            onError = errorHandler::handle
        )
    }

    ToolScreenScaffold(
        onSubmitClick = { onSubmit() },
        screenTitle = stringResource(R.string.url_tool_screen_title),
        onTopBarUpdate = onTopBarUpdate,
        submitEnabled = submitEnabled,
        submitButtonLabel = stringResource(R.string.analyze_button_label),
        submitButtonIconAlt = stringResource(R.string.analyze_button_icon_alt),
        modifier = modifier
    ) {
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
                                onSubmit()
                            }
                        }
                    )
            )
        }
    }
}