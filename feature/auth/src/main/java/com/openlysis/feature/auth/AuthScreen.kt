package com.openlysis.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.core.outcome.AppError
import com.openlysis.data.analysis.core.error.RepositoryError
import com.openlysis.feature.auth.component.HeroSection

/**
 * Authentication screen for sign-in or sign-up operations.
 *
 * @param viewModel The ViewModel managing authentication state and logic.
 * @param onAuthenticated Callback invoked when authentication is successful.
 * @param modifier Modifier for styling and layout.
 */
@Composable
internal fun AuthScreen(
    viewModel: AuthScreenViewModel,
    onAuthenticated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authState = uiState.authStatus
    if (authState is AuthenticationStatus.Success) {
        viewModel.persistCredentials()
        onAuthenticated()
    }

    val error =
        if (authState is AuthenticationStatus.Failure) {
            stringResource(getErrorResourceId(authState.error))
        } else {
            null
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxSize()
                .background(LocalAppColorScheme.current.background.brand.primary)
    ) {
        HeroSection(
            iconInlineVariant = true,
            modifier = Modifier.fillMaxWidth()
        )

        Content(
            emailValue = uiState.email,
            passwordValue = uiState.password,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onSubmitRequest = viewModel::authenticate,
            onSwitchAuthType = viewModel::switchAuthType,
            formEnabled = authState !is AuthenticationStatus.InProgress,
            submitEnabled = uiState.canAuthenticate,
            showLoading = authState is AuthenticationStatus.InProgress,
            errorMessage = error,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Content(
    emailValue: String,
    passwordValue: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmitRequest: () -> Unit,
    onSwitchAuthType: () -> Unit,
    formEnabled: Boolean,
    submitEnabled: Boolean,
    showLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val shape =
        RoundedCornerShape(
            topStart = LocalAppRadius.current.value400,
            topEnd = LocalAppRadius.current.value400
        )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .clip(shape)
                .background(
                    color = LocalAppColorScheme.current.background.default.primary,
                    shape = shape
                ).verticalScroll(scrollState)
                .padding(LocalAppSpacing.current.value800)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value600)
        ) {
            Text(
                text = stringResource(R.string.screen_auth_sign_in_title),
                style = LocalAppTypography.current.title3,
                color = LocalAppColorScheme.current.text.brand.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            FormInputs(
                emailValue = emailValue,
                passwordValue = passwordValue,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onPasswordImeSendRequest = { onSubmitRequest() }
            )

            FormErrorMessage(errorMessage)

            FormButton(
                onClick = onSubmitRequest,
                enabled = formEnabled && submitEnabled,
                showLoading = showLoading
            )
        }

        Spacer(modifier = Modifier.height(LocalAppSpacing.current.value600))

        AuthenticationTypeSwitch(
            contextLabel = stringResource(R.string.screen_auth_sign_in_switch_auth_type_label),
            buttonLabel =
                stringResource(
                    R.string.screen_auth_sign_in_switch_auth_type_button_label
                ),
            onSwitchAuthType = onSwitchAuthType
        )
    }
}

@Composable
private fun FormInputs(
    emailValue: String,
    passwordValue: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordImeSendRequest: KeyboardActionScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400)
    ) {
        TextInput(
            value = emailValue,
            onValueChange = onEmailChange,
            label = stringResource(R.string.screen_auth_email_input_label),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
        )

        PasswordInput(
            value = passwordValue,
            onValueChange = onPasswordChange,
            onImeSendRequest = onPasswordImeSendRequest,
            label = stringResource(R.string.screen_auth_password_input_label)
        )
    }
}

@Composable
private fun FormErrorMessage(
    error: String?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = error?.isNotBlank() == true,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        Text(
            text = error ?: "",
            style = LocalAppTypography.current.bodyBase,
            color = LocalAppColorScheme.current.text.danger.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FormButton(
    onClick: () -> Unit,
    enabled: Boolean,
    showLoading: Boolean
) {
    val submitButtonType =
        if (enabled) {
            ButtonType.Primary
        } else {
            ButtonType.PrimaryDisabled
        }

    AnimatedContent(
        targetState = showLoading,
        contentAlignment = Alignment.Center,
        transitionSpec = {
            scaleIn() + fadeIn() togetherWith
                fadeOut() + scaleOut()
        },
        modifier = Modifier.fillMaxWidth()
    ) { targetState ->
        if (targetState) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = LocalAppColorScheme.current.icon.brand.primary,
                    trackColor = LocalAppColorScheme.current.border.default.primary
                )
            }
        } else {
            AppButton(
                type = submitButtonType,
                size = SizeType.Default,
                onClick = onClick,
                displayLabel = true,
                displayIcon = true,
                label = stringResource(R.string.sign_in_button_label),
                icon = AppIcons.Login,
                iconAlt = stringResource(R.string.screen_auth_sign_in_primary_button_icon_alt),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AuthenticationTypeSwitch(
    contextLabel: String,
    buttonLabel: String,
    onSwitchAuthType: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200)
    ) {
        Text(
            text = contextLabel,
            style = LocalAppTypography.current.bodySmall,
            color = LocalAppColorScheme.current.text.default.secondary,
            textAlign = TextAlign.Center
        )

        AppButton(
            type = ButtonType.Secondary,
            size = SizeType.Small,
            onClick = onSwitchAuthType,
            displayLabel = true,
            displayIcon = false,
            label = buttonLabel
        )
    }
}

@Composable
private fun PasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    onImeSendRequest: KeyboardActionScope.() -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val passwordVisualTransformation = PasswordVisualTransformation()
    val passwordInputTransformation =
        if (showPassword) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        }
    val passwordButtonIcon =
        if (showPassword) {
            AppIcons.EyeOff
        } else {
            AppIcons.Eye
        }
    val passwordButtonIconAlt =
        if (showPassword) {
            R.string.screen_auth_password_input_hide_icon_alt
        } else {
            R.string.screen_auth_password_input_show_icon_alt
        }

    TextInput(
        value = value,
        onValueChange = onValueChange,
        label = label,
        trailingButton = {
            AppButton(
                type = ButtonType.Tertiary,
                size = SizeType.Small,
                onClick = { showPassword = !showPassword },
                displayLabel = false,
                displayIcon = true,
                icon = passwordButtonIcon,
                iconAlt = stringResource(passwordButtonIconAlt)
            )
        },
        visualTransformation = passwordInputTransformation,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Send
            ),
        keyboardActions = KeyboardActions(onSend = onImeSendRequest)
    )
}

private fun getErrorResourceId(error: AppError): Int =
    when (error) {
        is RepositoryError.AccessDenied -> R.string.error_sign_in
        is RepositoryError.Server -> R.string.error_server
        is RepositoryError.Network -> R.string.error_network
        is RepositoryError.ServerUnreachable -> R.string.error_server_unreachable
        is RepositoryError.Unavailable -> R.string.error_unavailable
        else -> R.string.error_generic
    }

@Preview(showSystemUi = true)
@Composable
private fun AuthScreenPreview() {
    OpenlysisTheme {
        Content(
            emailValue = "",
            passwordValue = "",
            onEmailChange = { },
            onPasswordChange = { },
            onSubmitRequest = { },
            onSwitchAuthType = { },
            formEnabled = true,
            submitEnabled = true,
            showLoading = false,
            errorMessage = "",
            modifier = Modifier.fillMaxSize()
        )
    }
}