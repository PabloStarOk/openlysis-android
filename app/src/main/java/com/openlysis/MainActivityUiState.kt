package com.openlysis

/**
 * The UI state for MainActivity.
 */
internal sealed interface MainActivityUiState {
    /**
     * UI state when the user is signed in.
     *
     * @property isUserSignedIn Indicates if the user is signed in.
     */
    data class Success(
        val userSignedIn: Boolean
    ) : MainActivityUiState

    /**
     * UI state when loading is in progress.
     */
    data object Loading : MainActivityUiState

    /**
     * Checks if the current state is Success and the user is signed in.
     *
     * @return true if the user is signed in, false otherwise.
     */
    fun isUserSignedIn() = this is Success && this.userSignedIn
}