package com.openlysis.feature.tools.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.core.outcome.AppError
import com.openlysis.data.analysis.core.error.RepositoryError
import com.openlysis.data.attachment.AttachmentCreationError
import com.openlysis.feature.tools.R
import com.openlysis.feature.tools.data.AnalysisRequestState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**
 * A composable dialog that displays the current state of an analysis request.
 *
 * @param state The current state of the analysis request.
 * @param onGoToAnalysisRequest Callback invoked when the user clicks the "Go to Analysis" button in success state.
 * @param onCancelRequest Callback invoked when the user cancels an in-progress analysis request.
 * @param onRetryRequest Callback invoked when the user clicks the retry button in failure state.
 * @param onDismissRequest Callback invoked when the dialog is dismissed.
 * @param modifier Optional [Modifier] to apply to the dialog.
 * @param enableCancelDelaySeconds Time in seconds before the cancel button becomes enabled (default: 60s).
 */
@Composable
internal fun AnalysisRequestStateDialog(
    state: AnalysisRequestState,
    onGoToAnalysisRequest: () -> Unit,
    onCancelRequest: () -> Unit,
    onRetryRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enableCancelDelaySeconds: Long = 60
) {
    var enableCancel by rememberSaveable { mutableStateOf(false) }
    val dialogState =
        remember(state, enableCancel) {
            DialogState(
                requestState = state,
                enableCancel = enableCancel,
                enableCancelDelaySeconds = enableCancelDelaySeconds
            )
        }
    val appColorScheme = LocalAppColorScheme.current
    val descriptionColor =
        remember(state, appColorScheme) {
            when (state) {
                is AnalysisRequestState.Failure -> appColorScheme.text.danger.secondary
                else -> appColorScheme.text.default.primary
            }
        }
    val showWaitingTime =
        remember(state, enableCancel) {
            !enableCancel &&
                (
                    state is AnalysisRequestState.InProgress ||
                        state is AnalysisRequestState.None
                )
        }

    Dialog(
        onDismissRequest = {
            if (dialogState.dismissible) {
                if (state is AnalysisRequestState.InProgress) {
                    onCancelRequest()
                }

                onDismissRequest()
            }
        }
    ) {
        Card(
            colors =
                CardDefaults
                    .cardColors()
                    .copy(containerColor = LocalAppColorScheme.current.background.default.primary),
            shape = RoundedCornerShape(LocalAppRadius.current.value100),
            modifier = modifier
        ) {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(
                        space = LocalAppSpacing.current.value600
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .padding(
                            all = LocalAppSpacing.current.value600
                        )
            ) {
                IndicationIcons(
                    requestState = state
                )

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            space = LocalAppSpacing.current.value050,
                            alignment = Alignment.CenterVertically
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.heightIn(min = 72.dp)
                ) {
                    Text(
                        text = stringResource(dialogState.description),
                        style = LocalAppTypography.current.bodyBase,
                        color = descriptionColor,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(visible = showWaitingTime) {
                        CountdownText(
                            onStart = { enableCancel = false },
                            onComplete = { enableCancel = true },
                            countdown = enableCancelDelaySeconds,
                            requestState = state,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                ActionButtons(
                    onGoToClick = onGoToAnalysisRequest,
                    onCancelClick = {
                        onCancelRequest()
                        onDismissRequest()
                    },
                    onRetryClick = onRetryRequest,
                    onBackClick = onDismissRequest,
                    requestState = state,
                    timeoutCompleted = enableCancel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun IndicationIcons(requestState: AnalysisRequestState) {
    val appColorScheme = LocalAppColorScheme.current
    val (icon, iconAltResId, iconColorResId) =
        remember(requestState, appColorScheme) {
            when (requestState) {
                is AnalysisRequestState.Success ->
                    Triple(
                        AppIconsIds.Check,
                        R.string.analysis_request_state_dialog_success_icon_alt,
                        appColorScheme.icon.positive.secondary
                    )
                else ->
                    Triple(
                        AppIconsIds.Cross,
                        R.string.analysis_request_state_dialog_error_icon_alt,
                        appColorScheme.icon.danger.secondary
                    )
            }
        }

    AnimatedContent(
        targetState = requestState,
        transitionSpec = {
            scaleIn() + fadeIn() togetherWith
                fadeOut() + scaleOut()
        },
        label = "analysis_request_state_dialog_indication_icons_animation"
    ) { state ->
        if (state is AnalysisRequestState.InProgress ||
            state is AnalysisRequestState.None
        ) {
            CircularProgressIndicator(
                color = LocalAppColorScheme.current.icon.brand.primary,
                trackColor = LocalAppColorScheme.current.border.default.primary,
                modifier = Modifier.size(50.dp)
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = stringResource(iconAltResId),
                tint = iconColorResId,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Composable
private fun CountdownText(
    onStart: () -> Unit,
    onComplete: () -> Unit,
    countdown: Long,
    requestState: AnalysisRequestState,
    modifier: Modifier = Modifier
) {
    var remainingSeconds by
        rememberSaveable {
            mutableLongStateOf(countdown)
        }

    LaunchedEffect(requestState) {
        if (requestState !is AnalysisRequestState.InProgress) return@LaunchedEffect

        onStart()

        while (remainingSeconds > 0) {
            delay(1.seconds)
            remainingSeconds--
        }

        onComplete()
    }

    Text(
        text =
            stringResource(
                R.string.analysis_request_state_dialog_waiting_time_description,
                remainingSeconds.seconds.toComponents {
                    _,
                    minutes,
                    seconds,
                    _
                    ->
                    "$minutes:${seconds.toString().padStart(2, '0')}"
                }
            ),
        style = LocalAppTypography.current.bodySmall,
        color = LocalAppColorScheme.current.text.default.secondary,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun ActionButtons(
    onGoToClick: () -> Unit,
    onCancelClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    requestState: AnalysisRequestState,
    timeoutCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val cancelButtonType =
        remember(timeoutCompleted) {
            if (timeoutCompleted) {
                ButtonType.Secondary
            } else {
                ButtonType.PrimaryDisabled
            }
        }

    val showRetryButton by
        remember(requestState) {
            derivedStateOf {
                requestState is AnalysisRequestState.Failure &&
                    requestState.error !is RepositoryError.BadRequest
            }
        }

    AnimatedContent(
        targetState = requestState,
        transitionSpec = {
            scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) +
                fadeIn(
                    animationSpec = tween(300)
                ) togetherWith scaleOut(
                    targetScale = 1.0f,
                    animationSpec = tween(200, easing = FastOutLinearInEasing)
                ) +
                fadeOut(
                    animationSpec = tween(200)
                )
        },
        label = "analysis_request_state_dialog_action_buttons_animation"
    ) { state ->
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    space = LocalAppSpacing.current.value300,
                    alignment = Alignment.End
                ),
            modifier = modifier
        ) {
            if (state is AnalysisRequestState.InProgress ||
                state is AnalysisRequestState.None
            ) {
                AppButton(
                    type = cancelButtonType,
                    size = SizeType.Small,
                    onClick = onCancelClick,
                    displayLabel = true,
                    displayIcon = true,
                    label =
                        stringResource(
                            R.string.analysis_request_state_dialog_cancel_button_label
                        ),
                    icon = AppIcons.Cross,
                    iconAlt =
                        stringResource(
                            R.string.analysis_request_state_dialog_cancel_button_icon_alt
                        )
                )
            }

            if (state !is AnalysisRequestState.InProgress &&
                state !is AnalysisRequestState.None
            ) {
                AppButton(
                    type = ButtonType.Secondary,
                    size = SizeType.Small,
                    onClick = onBackClick,
                    displayLabel = true,
                    displayIcon = true,
                    label =
                        stringResource(
                            R.string.analysis_request_state_dialog_back_button_label
                        ),
                    icon = AppIcons.Back,
                    iconAlt =
                        stringResource(
                            R.string.analysis_request_state_dialog_back_button_icon_alt
                        )
                )
            }

            if (state is AnalysisRequestState.Success) {
                AppButton(
                    type = ButtonType.Primary,
                    size = SizeType.Small,
                    onClick = onGoToClick,
                    displayLabel = true,
                    displayIcon = true,
                    label =
                        stringResource(
                            R.string.analysis_request_state_dialog_go_button_label
                        ),
                    icon = AppIcons.Open,
                    iconAlt =
                        stringResource(
                            R.string.analysis_request_state_dialog_go_button_icon_alt
                        )
                )
            }

            if (state is AnalysisRequestState.Failure && showRetryButton) {
                AppButton(
                    type = ButtonType.Primary,
                    size = SizeType.Small,
                    onClick = onRetryClick,
                    displayLabel = true,
                    displayIcon = true,
                    label =
                        stringResource(
                            R.string.analysis_request_state_dialog_retry_button_label
                        ),
                    icon = AppIcons.Retry,
                    iconAlt =
                        stringResource(
                            R.string.analysis_request_state_dialog_retry_button_icon_alt
                        )
                )
            }
        }
    }
}

/**
 * Represents the state of the analysis request dialog.
 *
 * @property requestState The current state of the analysis request.
 * @property enableCancel Whether the cancel button should be enabled.
 * @property enableCancelDelaySeconds The delay in seconds before enabling the cancel button.
 */
private data class DialogState(
    val requestState: AnalysisRequestState,
    val enableCancel: Boolean,
    val enableCancelDelaySeconds: Long
) {
    val dismissible: Boolean = requestState !is AnalysisRequestState.InProgress || enableCancel
    val description: Int =
        when (requestState) {
            is AnalysisRequestState.Success ->
                R.string.analysis_request_state_dialog_success_description
            is AnalysisRequestState.Failure -> getAnalysisErrorString(requestState.error)
            else -> R.string.analysis_request_state_dialog_in_progress_description
        }

    private fun getAnalysisErrorString(error: AppError): Int =
        when (error) {
            is RepositoryError -> getRepositoryErrorString(error)
            is AttachmentCreationError -> R.string.error_attachment_creation
            else -> R.string.error_analysis_repository_generic
        }

    private fun getRepositoryErrorString(error: RepositoryError): Int =
        when (error) {
            RepositoryError.BadRequest -> R.string.error_analysis_repository_bad_request
            RepositoryError.AccessDenied -> R.string.error_analysis_repository_generic
            RepositoryError.Network -> R.string.error_analysis_repository_network
            RepositoryError.NotFound -> R.string.error_analysis_repository_generic
            RepositoryError.OperationCanceled ->
                R.string.error_analysis_repository_operation_canceled
            RepositoryError.Server -> R.string.error_analysis_repository_server
            RepositoryError.ServerUnreachable ->
                R.string.error_analysis_repository_unreachable_server
            RepositoryError.Unavailable -> R.string.error_analysis_repository_unavailable
            RepositoryError.Unknown -> R.string.error_analysis_repository_unknown
        }
}

@PreviewLightDark
@Preview(showSystemUi = false)
@Composable
private fun AnalysisProgressDialogPreview() {
    OpenlysisTheme {
        AnalysisRequestStateDialog(
            state = AnalysisRequestState.InProgress,
            onGoToAnalysisRequest = { },
            onCancelRequest = { },
            onRetryRequest = { },
            onDismissRequest = { }
        )
    }
}