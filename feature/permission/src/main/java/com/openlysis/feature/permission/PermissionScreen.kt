package com.openlysis.feature.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * Composable screen for requesting a specific permission from the user.
 *
 * @param onPermissionAllowed Callback invoked when the permission is granted.
 * @param onSkipRequest Callback invoked when the user skips or denies the permission request.
 * @param permission The permission string to request (e.g., android.permission.SMS).
 * @param heroIcon The icon to display in the hero section.
 * @param heroIconAlt Content description for the hero icon.
 * @param heroTitle Title text for the hero section.
 * @param heroDescription Description text for the hero section.
 * @param illustration The illustration image to display.
 * @param illustrationAlt Content description for the illustration.
 * @param allowLabel Label for the allow button.
 * @param skipLabel Label for the skip button.
 * @param modifier Modifier to be applied to the root composable.
 */
@Composable
internal fun PermissionScreen(
    onPermissionAllowed: () -> Unit,
    onSkipRequest: () -> Unit,
    permission: String,
    heroIcon: ImageVector,
    heroIconAlt: String,
    heroTitle: String,
    heroDescription: String,
    illustration: ImageVector,
    illustrationAlt: String,
    allowLabel: String,
    skipLabel: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val permissionContract = ActivityResultContracts.RequestPermission()
    val permissionLauncher =
        rememberLauncherForActivityResult(permissionContract) { allowed ->
            if (allowed) {
                onPermissionAllowed()
            } else {
                onSkipRequest()
            }
        }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(LocalAppSpacing.current.value400)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Hero(
                icon = heroIcon,
                iconAlt = heroIconAlt,
                title = heroTitle,
                description = heroDescription
            )

            Illustration(
                illustration = illustration,
                illustrationAlt = illustrationAlt,
                modifier = modifier
            )
        }

        Buttons(
            onAllowClick = { permissionLauncher.launch(permission) },
            onSkipClick = onSkipRequest,
            allowLabel = allowLabel,
            skipLabel = skipLabel
        )
    }
}

@Composable
private fun Hero(
    icon: ImageVector,
    iconAlt: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconAlt,
            tint = LocalAppColorScheme.current.icon.brand.primary
        )

        Text(
            text = title,
            style = LocalAppTypography.current.title5,
            color = LocalAppColorScheme.current.text.brand.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = description,
            style = LocalAppTypography.current.bodyBase,
            color = LocalAppColorScheme.current.text.default.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Illustration(
    illustration: ImageVector,
    illustrationAlt: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .background(
                    color = LocalAppColorScheme.current.background.default.secondary,
                    shape = RoundedCornerShape(LocalAppRadius.current.full)
                ).width(IntrinsicSize.Min)
    ) {
        Image(
            imageVector = illustration,
            contentDescription = illustrationAlt,
            modifier =
                Modifier
                    .widthIn(max = 250.dp)
                    .size(250.dp)
        )
    }
}

@Composable
private fun Buttons(
    onAllowClick: () -> Unit,
    onSkipClick: () -> Unit,
    allowLabel: String,
    skipLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        AppButton(
            type = ButtonType.Tertiary,
            size = SizeType.Default,
            onClick = onSkipClick,
            displayLabel = true,
            displayIcon = false,
            label = skipLabel
        )

        AppButton(
            type = ButtonType.Primary,
            size = SizeType.Default,
            onClick = onAllowClick,
            displayLabel = true,
            displayIcon = false,
            label = allowLabel
        )
    }
}

@PreviewLightDark
@Composable
private fun PermissionScreenScaffoldPreview() {
    OpenlysisTheme {
        PermissionScreen(
            onPermissionAllowed = { },
            onSkipRequest = { },
            permission = "Any",
            heroIcon = AppIcons.Sms,
            heroIconAlt = "SMS icon",
            heroTitle = "Title",
            heroDescription = "A description of the permission.",
            illustration = ImageVector.vectorResource(R.drawable.illustration_sms),
            illustrationAlt = "An illustration.",
            allowLabel = "Allow",
            skipLabel = "Skip"
        )
    }
}