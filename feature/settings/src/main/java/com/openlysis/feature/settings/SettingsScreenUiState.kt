package com.openlysis.feature.settings

import com.openlysis.data.user.model.ThemeConfig

/**
 * UI state for the Settings screen.
 *
 * @property themeConfig The current theme configuration for the app.
 */
internal data class SettingsScreenUiState(
    val themeConfig: ThemeConfig = ThemeConfig.System
)