package com.openlysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.user.UserDataRepository
import com.openlysis.data.user.model.Permission
import com.openlysis.navigation.AppPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for MainActivity.
 *
 * @param authTokensManager An [AuthTokensManager] to observe user authentication state.
 * @param userDataRepository Repository to observe user data.
 */
@HiltViewModel
internal class MainActivityViewModel
    @Inject
    constructor(
        authTokensManager: AuthTokensManager,
        userDataRepository: UserDataRepository
    ) : ViewModel() {
        val uiState: StateFlow<MainActivityUiState> =
            authTokensManager.data
                .combine(
                    userDataRepository.data
                ) { authTokens, userData ->
                    val pendingPermissions =
                        AppPermission.entries
                            .filter { shouldAskPermission(it, userData.askedPermissions) }
                            .sortedByDescending { it.ordinal }

                    MainActivityUiState.Success(
                        userSignedIn = authTokens.canRefresh,
                        userThemeConfig = userData.themeConfig,
                        pendingPermissions = pendingPermissions
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = MainActivityUiState.Loading
                )

        private fun shouldAskPermission(
            target: AppPermission,
            askedPermissions: List<Permission>
        ): Boolean {
            val askedPermission = askedPermissions.find { it.manifestName == target.manifestName }
            if (askedPermission == null) return true

            val userDeniedPermissionFromSystem =
                askedPermission.wasGrantedFromApp && !askedPermission.isGrantedFromSystem
            return userDeniedPermissionFromSystem
        }
    }