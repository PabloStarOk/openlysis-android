package com.openlysis.feature.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.UserAuthData
import com.openlysis.data.auth.UserAuthDataRepository
import com.openlysis.data.auth.UserAuthenticator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the authentication screen.
 *
 * @param authenticator Handles user authentication logic.
 * @param userAuthDataRepository Repository for persisting user authentication data.
 * @param authType The type of authentication (SignIn or SignUp) for this ViewModel instance.
 */
@HiltViewModel(assistedFactory = AuthScreenViewModel.Factory::class)
internal class AuthScreenViewModel
    @AssistedInject
    constructor(
        private val authenticator: UserAuthenticator,
        private val userAuthDataRepository: UserAuthDataRepository,
        @Assisted authType: AuthenticationType
    ) : ViewModel() {
        private var userAuthData = UserAuthData(isSignedIn = false, apiKey = null)
        private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState(authType))
        val uiState = _uiState.asStateFlow()

        init {
            validateData()
        }

        /**
         * Initiates the authentication process based on the current authentication type.
         */
        fun authenticate() {
            viewModelScope.launch {
                when (uiState.value.authType) {
                    AuthenticationType.SignUp -> {
                        TODO("Add sign up functionality.")
                    }
                    AuthenticationType.SignIn -> signIn()
                }
            }
        }

        /**
         * Switches the authentication type between SignIn and SignUp.
         */
        fun switchAuthType() {
            _uiState.update {
                val targetType =
                    when (it.authType) {
                        AuthenticationType.SignUp -> AuthenticationType.SignIn
                        AuthenticationType.SignIn -> AuthenticationType.SignUp
                    }
                it.copy(
                    authType = targetType
                )
            }
        }

        /**
         * Updates the email in the UI state.
         *
         * @param email The new email address entered by the user.
         */
        fun updateEmail(email: String) {
            _uiState.update { it.copy(email = email) }
        }

        /**
         * Updates the password in the UI state.
         *
         * @param password The new password entered by the user.
         */
        fun updatePassword(password: String) {
            _uiState.update { it.copy(password = password) }
        }

        /**
         * Persists the user's credentials by saving the API key and signed-in state
         * to the repository if authentication was successful.
         */
        fun persistCredentials() {
            if (userAuthData.apiKey == null ||
                uiState.value.authStatus !is AuthenticationStatus.Success
            ) {
                return
            }

            viewModelScope.launch {
                userAuthDataRepository.saveApiKey(userAuthData.apiKey as String)
                userAuthDataRepository.setSignedIn(userAuthData.isSignedIn)
            }
        }

        private fun validateData() {
            _uiState
                .distinctUntilChanged { old, new ->
                    old.email != new.email &&
                        old.password != new.password
                }.onEach {
                    _uiState.update {
                        val canAuthenticate =
                            when (it.authType) {
                                AuthenticationType.SignUp -> TODO("Add sign-up validation")
                                AuthenticationType.SignIn -> canSignIn(it.email, it.password)
                            }
                        it.copy(canAuthenticate = canAuthenticate)
                    }
                }.launchIn(viewModelScope)
        }

        private suspend fun signIn() {
            _uiState.update { it.copy(authStatus = AuthenticationStatus.InProgress) }
            val outcome =
                authenticator.signIn(
                    email = uiState.value.email,
                    password = uiState.value.password
                )

            when (outcome) {
                is Outcome.Success -> {
                    userAuthData =
                        UserAuthData(
                            isSignedIn = true,
                            apiKey = outcome.value
                        )
                    _uiState.update {
                        it.copy(
                            authStatus = AuthenticationStatus.Success
                        )
                    }
                }
                is Outcome.Failure ->
                    _uiState.update {
                        it.copy(
                            authStatus = AuthenticationStatus.Failure(outcome.error)
                        )
                    }
            }
        }

        private fun canSignIn(
            email: String,
            password: String
        ): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()

        /**
         * Factory interface for creating instances of [AuthScreenViewModel] with a specific [AuthenticationType].
         */
        @AssistedFactory
        interface Factory {
            /**
             * Creates an [AuthScreenViewModel] with the provided [AuthenticationType].
             *
             * @param authenticationType The type of authentication (SignIn or SignUp).
             * @return A new instance of [AuthScreenViewModel].
             */
            fun create(authenticationType: AuthenticationType): AuthScreenViewModel
        }
    }