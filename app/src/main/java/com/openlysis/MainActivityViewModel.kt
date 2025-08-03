package com.openlysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.auth.UserAuthDataRepository
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
 * @param userAuthDataRepository Repository to observe user authentication state.
 * @param userDataRepository Repository to observe user data.
 */
@HiltViewModel
internal class MainActivityViewModel
    @Inject
    constructor(
        userAuthDataRepository: UserAuthDataRepository,
        userDataRepository: UserDataRepository
    ) : ViewModel() {
        val uiState: StateFlow<MainActivityUiState> =
            userAuthDataRepository.data
                .combine(
                    userDataRepository.data
                ) { userAuthData, userData ->
                    val pendingPermissions =
                        AppPermission.entries
                            .filter { shouldAskPermission(it, userData.askedPermissions) }
                            .sortedByDescending { it.ordinal }

                    MainActivityUiState.Success(
                        userSignedIn = userAuthData.isSignedIn,
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