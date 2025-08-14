package com.openlysis

import com.openlysis.navigation.AppPermission

/**
 * The UI state for MainActivity.
 */
internal sealed interface MainActivityUiState {
    /**
     * Success UI state.
     *
     * @property userSignedIn Indicates if the user is signed in.
     * @property pendingPermissions Permissions pending to be asked to the user.
     */
    data class Success(
        val userSignedIn: Boolean,
        val pendingPermissions: List<AppPermission>
    ) : MainActivityUiState

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
}