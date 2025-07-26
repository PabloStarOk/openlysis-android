package com.openlysis.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.openlysis.core.outcome.NetworkError
import com.openlysis.feature.auth.component.HeroSection
import com.openlysis.feature.auth.model.AuthenticationStatus
import com.openlysis.feature.auth.model.AuthenticationType
import com.openlysis.feature.auth.model.FormResourcesIds
import com.openlysis.feature.auth.model.PasswordRequirement
import com.openlysis.feature.auth.model.PasswordRequirementType

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
            uiState = uiState,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onSubmitRequest = viewModel::authenticate,
            onSwitchAuthType = viewModel::switchAuthType,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Content(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: ((String) -> Unit),
    onSubmitRequest: () -> Unit,
    onSwitchAuthType: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val shape =
        RoundedCornerShape(
            topStart = LocalAppRadius.current.value400,
            topEnd = LocalAppRadius.current.value400
        )
    val formResourceIds = formResourceIdsMap.getValue(uiState.authType)
    val authState = uiState.authStatus
    val errorMessage =
        if (authState is AuthenticationStatus.Failure) {
            stringResource(getErrorResourceId(authState.error))
        } else {
            null
        }

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
                text = stringResource(formResourceIds.title),
                style = LocalAppTypography.current.title3,
                color = LocalAppColorScheme.current.text.brand.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            FormInputs(
                emailValue = uiState.email,
                passwordValue = uiState.password,
                confirmPasswordValue = uiState.confirmPassword,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onPasswordImeSendRequest = { onSubmitRequest() },
                isSignUp = uiState.authType == AuthenticationType.SignUp,
                passwordsMatch = uiState.passwordsMatch,
                passwordRequirements = uiState.passwordRequirements
            )

            FormErrorMessage(errorMessage)

            FormButton(
                onClick = onSubmitRequest,
                label = stringResource(formResourceIds.submitLabel),
                enabled = authState !is AuthenticationStatus.InProgress && uiState.canAuthenticate,
                showLoading = authState is AuthenticationStatus.InProgress
            )
        }

        Spacer(modifier = Modifier.height(LocalAppSpacing.current.value600))

        AuthenticationTypeSwitch(
            contextLabel = stringResource(formResourceIds.switchTypeLabel),
            buttonLabel = stringResource(formResourceIds.switchTypeButtonLabel),
            onSwitchAuthType = onSwitchAuthType
        )
    }
}

@Composable
private fun FormInputs(
    emailValue: String,
    passwordValue: String,
    confirmPasswordValue: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: ((String) -> Unit),
    onPasswordImeSendRequest: (KeyboardActionScope.() -> Unit)?,
    passwordRequirements: List<PasswordRequirement>,
    passwordsMatch: Boolean,
    isSignUp: Boolean
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
            label = stringResource(R.string.screen_auth_password_input_label),
            isError = false,
            imeAction = if (isSignUp) ImeAction.Next else ImeAction.Send,
            keyboardActions =
                if (isSignUp) {
                    KeyboardActions.Default
                } else {
                    KeyboardActions(onSend = onPasswordImeSendRequest)
                },
            supportingText = {
                AnimatedVisibility(
                    visible = isSignUp,
                    enter = expandVertically(animationSpec = tween(200)),
                    exit = shrinkVertically(animationSpec = tween(200))
                ) {
                    PasswordRequirementsList(
                        requirements = passwordRequirements,
                        modifier =
                            Modifier.padding(
                                horizontal = LocalAppSpacing.current.value200
                            )
                    )
                }
            }
        )

        AnimatedVisibility(
            visible = isSignUp,
            enter =
                expandVertically(
                    animationSpec = tween(200),
                    clip = false
                ) + fadeIn(animationSpec = tween(200)),
            exit =
                shrinkVertically(
                    animationSpec = tween(200),
                    clip = false
                ) + fadeOut(animationSpec = tween(200))
        ) {
            PasswordInput(
                value = confirmPasswordValue,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.screen_auth_sign_up_confirm_password_input_label),
                imeAction = ImeAction.Send,
                keyboardActions = KeyboardActions(onSend = onPasswordImeSendRequest),
                isError = !passwordsMatch,
                supportingText = {
                    AnimatedVisibility(
                        visible = !passwordsMatch,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Text(
                            text =
                                stringResource(
                                    R.string.screen_auth_sign_up_passwords_do_not_match
                                ),
                            style = LocalAppTypography.current.bodySmall,
                            color = LocalAppColorScheme.current.text.danger.secondary,
                            modifier =
                                Modifier.padding(
                                    horizontal = LocalAppSpacing.current.value200
                                )
                        )
                    }
                }
            )
        }
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
    label: String,
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
                label = label,
                icon = AppIcons.Login,
                iconAlt = stringResource(R.string.screen_auth_primary_button_icon_alt),
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
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    supportingText: (@Composable () -> Unit)?,
    isError: Boolean,
    modifier: Modifier = Modifier
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
        isError = isError,
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
                imeAction = imeAction
            ),
        keyboardActions = keyboardActions,
        supportingText = supportingText,
        modifier = modifier
    )
}

@Composable
private fun PasswordRequirementsList(
    requirements: List<PasswordRequirement>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    val iconDegrees by animateFloatAsState(if (expanded) 180f else 0f)
    val headerIconAltResId =
        if (expanded) {
            R.string.password_requirements_expanded_chevron_icon_alt
        } else {
            R.string.password_requirements_collapsed_chevron_icon_alt
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value050),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = expanded,
                        role = Role.Button,
                        onValueChange = { targetValue -> expanded = targetValue }
                    )
        ) {
            Text(
                text = stringResource(R.string.password_requirements),
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.default.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = AppIcons.ChevronDown,
                contentDescription = stringResource(headerIconAltResId),
                tint = LocalAppColorScheme.current.icon.default.primary,
                modifier =
                    Modifier
                        .rotate(iconDegrees)
                        .size(20.dp)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = LocalAppSpacing.current.value300)
            ) {
                requirements.forEach { PasswordRequirementItem(it) }
            }
        }
    }
}

@Composable
private fun PasswordRequirementItem(
    requirement: PasswordRequirement,
    modifier: Modifier = Modifier
) {
    val foregroundColor by animateColorAsState(
        if (requirement.isSatisfied) {
            LocalAppColorScheme.current.text.positive.secondary
        } else {
            LocalAppColorScheme.current.text.default.primary
        }
    )
    val iconAltResId =
        if (requirement.isSatisfied) {
            R.string.password_requirements_met_icon_alt
        } else {
            R.string.password_requirements_unmet_icon_alt
        }
    val label =
        pluralStringResource(
            requirementLabelResourceIdsMap.getValue(requirement.type),
            requirement.type.target,
            requirement.type.target
        )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.spacedBy(LocalAppSpacing.current.value100),
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = if (requirement.isSatisfied) AppIcons.Check else AppIcons.Cross,
            contentDescription = stringResource(iconAltResId),
            tint = foregroundColor,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = label,
            style = LocalAppTypography.current.bodySmall,
            color = foregroundColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getErrorResourceId(error: AppError): Int =
    when (error) {
        is NetworkError.BadRequest -> R.string.error_sign_up
        is NetworkError.AccessDenied -> R.string.error_sign_in
        is NetworkError.Server -> R.string.error_server
        is NetworkError.Network -> R.string.error_network
        is NetworkError.ServerUnreachable -> R.string.error_server_unreachable
        is NetworkError.Unavailable -> R.string.error_unavailable
        else -> R.string.error_generic
    }

private val formResourceIdsMap =
    mapOf(
        Pair(
            AuthenticationType.SignUp,
            FormResourcesIds(
                title = R.string.screen_auth_sign_up_title,
                submitLabel = R.string.sign_up_button_label,
                switchTypeLabel = R.string.screen_auth_sign_up_switch_auth_type_label,
                switchTypeButtonLabel = R.string.screen_auth_sign_up_switch_auth_type_button_label
            )
        ),
        Pair(
            AuthenticationType.SignIn,
            FormResourcesIds(
                title = R.string.screen_auth_sign_in_title,
                submitLabel = R.string.sign_in_button_label,
                switchTypeLabel = R.string.screen_auth_sign_in_switch_auth_type_label,
                switchTypeButtonLabel = R.string.screen_auth_sign_in_switch_auth_type_button_label
            )
        )
    )

private val requirementLabelResourceIdsMap =
    mapOf(
        Pair(PasswordRequirementType.MinLength, R.plurals.password_requirements_min_length),
        Pair(
            PasswordRequirementType.MinLowerLetters,
            R.plurals.password_requirements_min_lower_letters
        ),
        Pair(
            PasswordRequirementType.MinUpperLetters,
            R.plurals.password_requirements_min_upper_letters
        ),
        Pair(PasswordRequirementType.MinDigits, R.plurals.password_requirements_min_digits),
        Pair(
            PasswordRequirementType.MinSpecialChars,
            R.plurals.password_requirements_min_special_chars
        )
    )

@Preview(showSystemUi = true)
@Composable
private fun AuthScreenPreview() {
    OpenlysisTheme {
        Content(
            uiState = AuthUiState(authType = AuthenticationType.SignUp),
            onEmailChange = { },
            onPasswordChange = { },
            onConfirmPasswordChange = { },
            onSubmitRequest = { },
            onSwitchAuthType = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}