package com.openlysis.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.ConfirmationDialog
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.AppRadioButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.data.user.model.ThemeConfig
import com.openlysis.feature.settings.component.SettingsSection

/**
 * Displays the Settings screen.
 *
 * @param viewModel The ViewModel providing UI state and actions.
 * @param modifier Modifier for styling the composable.
 */
@Composable
internal fun SettingsScreen(
    viewModel: SettingsScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        selectedTheme = uiState.themeConfig,
        onThemeSelected = viewModel::setTheme,
        onSignOutRequest = viewModel::signOut,
        modifier = modifier
    )
}

/**
 * Composable for the main Settings screen content.
 *
 * @param selectedTheme The currently selected theme configuration.
 * @param onThemeSelected Callback invoked when a theme is selected.
 * @param onSignOutRequest Callback invoked when sign out is requested.
 * @param modifier Modifier for styling the composable.
 */
@Composable
private fun SettingsScreen(
    selectedTheme: ThemeConfig,
    onThemeSelected: (ThemeConfig) -> Unit,
    onSignOutRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSignOutConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value800),
        modifier = modifier.padding(LocalAppSpacing.current.value400)
    ) {
        ThemeSection(
            selectedTheme = selectedTheme,
            onThemeSelected = onThemeSelected
        )
        AccountSection(onSignOutRequest = { showSignOutConfirmationDialog = true })
    }

    if (showSignOutConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.screen_settings_section_account_dialog_title_sign_out),
            description =
                stringResource(
                    R.string.screen_settings_section_account_dialog_description_sign_out
                ),
            onConfirm = {
                showSignOutConfirmationDialog = false
                onSignOutRequest()
            },
            onCancel = { showSignOutConfirmationDialog = false }
        )
    }
}

@Composable
private fun ThemeSection(
    selectedTheme: ThemeConfig,
    onThemeSelected: (ThemeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsSection(
        title = stringResource(R.string.screen_settings_section_theme_title),
        modifier = modifier
    ) {
        AppRadioButton(
            onClick = { onThemeSelected(ThemeConfig.System) },
            selected = selectedTheme == ThemeConfig.System,
            label = stringResource(R.string.screen_settings_section_theme_label_system),
            modifier = Modifier.fillMaxWidth()
        )

        AppRadioButton(
            onClick = { onThemeSelected(ThemeConfig.Light) },
            selected = selectedTheme == ThemeConfig.Light,
            label = stringResource(R.string.screen_settings_section_theme_label_light),
            modifier = Modifier.fillMaxWidth()
        )

        AppRadioButton(
            onClick = { onThemeSelected(ThemeConfig.Dark) },
            selected = selectedTheme == ThemeConfig.Dark,
            label = stringResource(R.string.screen_settings_section_theme_label_dark),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AccountSection(
    onSignOutRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsSection(
        title = stringResource(R.string.screen_settings_section_account_title),
        modifier = modifier
    ) {
        AppButton(
            type = ButtonType.Secondary,
            size = SizeType.Default,
            onClick = onSignOutRequest,
            displayLabel = true,
            displayIcon = true,
            label = stringResource(R.string.screen_settings_section_account_button_label_sign_out),
            icon = AppIcons.LogOut,
            iconAlt =
                stringResource(R.string.screen_settings_section_account_button_icon_alt_sign_out),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewSettingsScreen() {
    OpenlysisTheme {
        SettingsScreen(
            selectedTheme = ThemeConfig.Dark,
            onThemeSelected = { },
            onSignOutRequest = { }
        )
    }
}