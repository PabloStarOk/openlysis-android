package com.openlysis.data.remote

import com.openlysis.data.auth.UserAuthenticator
import com.openlysis.data.remote.dto.request.SignInRequest
import com.openlysis.data.remote.dto.request.SignUpRequest
import javax.inject.Inject

/**
 * Default implementation of [UserAuthenticator] that uses [AuthenticationApi] to perform
 * user authentication operations such as sign up and sign in.
 *
 * @property api The API interface for authentication-related network requests.
 */
internal class DefaultUserAuthenticator
    @Inject
    constructor(
        private val api: AuthenticationApi
    ) : NetworkApiCaller(),
        UserAuthenticator {
        override suspend fun signUp(
            email: String,
            password: String
        ) = callApiSafely {
            api.signUp(
                SignUpRequest(
                    userName = email,
                    email = email,
                    password = password
                )
            )
        }

        override suspend fun signIn(
            email: String,
            password: String
        ) = callApiSafely {
            val response =
                api.signIn(
                    SignInRequest(
                        userName = email,
                        password = password
                    )
                )

            convertToModelIfSuccess(response) { it.apiKey }
        }
    }