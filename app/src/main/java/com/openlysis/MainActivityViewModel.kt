package com.openlysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.data.auth.UserAuthDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for MainActivity.
 *
 * Injects [UserAuthDataRepository] to observe user authentication state.
 * Uses Hilt for dependency injection.
 */
@HiltViewModel
internal class MainActivityViewModel
    @Inject
    constructor(
        userAuthDataRepository: UserAuthDataRepository
    ) : ViewModel() {
        val uiState =
            userAuthDataRepository.data
                .map {
                    MainActivityUiState.Success(it.isSignedIn)
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = MainActivityUiState.Loading
                )
    }