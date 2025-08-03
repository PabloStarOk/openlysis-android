package com.openlysis.feature.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.user.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the permission screen.
 *
 * Handles permission-related logic and interacts with the UserDataRepository
 * to update and persist permission states.
 *
 * @property userDataRepository Repository for user data operations.
 */
@HiltViewModel
internal class PermissionScreenViewModel
    @Inject
    constructor(
        private val userDataRepository: UserDataRepository
    ) : ViewModel() {
        /**
         * Updates the permission status for a given permission.
         *
         * @param permission The name of the permission to update.
         * @param granted Whether the permission was granted by the user.
         */
        fun setPermission(
            permission: String,
            granted: Boolean
        ) {
            viewModelScope.launch {
                userDataRepository.addOrUpdatedAskedPermission(permission, granted)
            }
        }
    }