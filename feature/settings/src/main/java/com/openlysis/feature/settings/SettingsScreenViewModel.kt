package com.openlysis.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.user.UserDataRepository
import com.openlysis.data.user.model.ThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 *
 * @property userDataRepository Repository for accessing and updating user data.
 * @property authTokensManager Manager for handling authentication tokens.
 */
@HiltViewModel
internal class SettingsScreenViewModel
    @Inject
    constructor(
        private val userDataRepository: UserDataRepository,
        private val authTokensManager: AuthTokensManager
    ) : ViewModel() {
        val uiState =
            userDataRepository.data
                .map {
                    SettingsScreenUiState(it.themeConfig)
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_0000),
                    initialValue = SettingsScreenUiState()
                )

        fun setTheme(themeConfig: ThemeConfig) {
            if (themeConfig == uiState.value.themeConfig) return

            viewModelScope.launch {
                userDataRepository.setThemeConfig(themeConfig)
            }
        }

        fun signOut() {
            viewModelScope.launch {
                authTokensManager.deleteTokens()
            }
        }
    }