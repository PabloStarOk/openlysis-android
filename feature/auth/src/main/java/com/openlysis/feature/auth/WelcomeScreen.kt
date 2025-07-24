package com.openlysis.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.auth.component.HeroSection

/**
 * Displays the welcome screen for authentication.
 *
 * @param onGoToSignUp Callback invoked when the sign up button is pressed.
 * @param onGoToSignIn Callback invoked when the sign in button is pressed.
 * @param modifier Modifier to be applied to the root composable.
 */
@Composable
internal fun WelcomeScreen(
    onGoToSignUp: () -> Unit,
    onGoToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier
                .background(LocalAppColorScheme.current.background.brand.primary)
                .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeroSection(
                iconInlineVariant = false,
                modifier = Modifier.weight(1f).fillMaxSize()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = LocalAppColorScheme.current.background.default.primary,
                            shape =
                                RoundedCornerShape(
                                    topStart = LocalAppRadius.current.value400,
                                    topEnd = LocalAppRadius.current.value400
                                )
                        ).padding(
                            vertical = LocalAppSpacing.current.value600,
                            horizontal = LocalAppSpacing.current.value800
                        )
            ) {
                InformationSection()

                ActionButtons(onGoToSignUp, onGoToSignIn)
            }
        }
    }
}

@Composable
private fun InformationSection(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value150
            ),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.screen_welcome_title),
            color = LocalAppColorScheme.current.text.brand.primary,
            style = LocalAppTypography.current.title3,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.screen_welcome_description),
            color = LocalAppColorScheme.current.text.default.primary,
            style = LocalAppTypography.current.bodyBase,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ActionButtons(
    onGoToSignUp: () -> Unit,
    onGoToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value400),
        modifier = modifier
    ) {
        AppButton(
            type = ButtonType.Primary,
            size = SizeType.Default,
            onClick = onGoToSignUp,
            displayLabel = true,
            displayIcon = false,
            label = stringResource(R.string.sign_up_button_label),
            modifier = Modifier.fillMaxWidth()
        )

        AppButton(
            type = ButtonType.Secondary,
            size = SizeType.Default,
            onClick = onGoToSignIn,
            displayLabel = true,
            displayIcon = false,
            label = stringResource(R.string.sign_in_button_label),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true, device = "spec:parent=pixel_5,navigation=buttons")
@Composable
private fun WelcomeScreenPreview() {
    OpenlysisTheme {
        WelcomeScreen(
            onGoToSignUp = { },
            onGoToSignIn = { }
        )
    }
}