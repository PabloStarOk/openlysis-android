package com.openlysis

import com.openlysis.data.user.model.ThemeConfig
import com.openlysis.navigation.AppPermission

/**
 * The UI state for MainActivity.
 */
internal sealed interface MainActivityUiState {
    /**
     * Success UI state.
     *
     * @property userSignedIn Indicates if the user is signed in.
     * @property userThemeConfig The user's selected theme configuration.
     * @property pendingPermissions Permissions pending to be asked to the user.
     */
    data class Success(
        val userSignedIn: Boolean,
        var userThemeConfig: ThemeConfig,
        val pendingPermissions: List<AppPermission>
    ) : MainActivityUiState {
        /**
         * Determines if the dark theme should be used based on the user's theme configuration.
         *
         * @param isSystemInDarkTheme Indicates if the system is currently in dark theme.
         * @return true if the user's theme config is dark, false if light, or matches the system setting.
         */
        override fun shouldUseDarkTheme(isSystemInDarkTheme: Boolean) =
            when (userThemeConfig) {
                ThemeConfig.System -> isSystemInDarkTheme
                ThemeConfig.Light -> false
                ThemeConfig.Dark -> true
            }
    }

    /**
     * UI state when loading is in progress.
     */
    data object Loading : MainActivityUiState

    /**
     * Determines if authentication is required.
     *
     * @return true if the current state is Success and the user is not signed in, false otherwise.
     */
    fun shouldAuthenticate() = this is Success && !this.userSignedIn

    /**
     * Checks if the splash screen should be kept visible.
     *
     * @return true if the current state is Loading, false otherwise.
     */
    fun shouldKeepSplashScreen() = this is Loading

    /**
     * Determines if the dark theme should be used.
     *
     * @param isSystemInDarkTheme Indicates if the system is currently in dark theme.
     * @return true if the dark theme should be used, false otherwise.
     */
    fun shouldUseDarkTheme(isSystemInDarkTheme: Boolean) = isSystemInDarkTheme
}