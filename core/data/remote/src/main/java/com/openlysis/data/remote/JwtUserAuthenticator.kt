package com.openlysis.data.remote

import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.auth.UserAuthenticator
import com.openlysis.data.auth.model.AuthTokens
import com.openlysis.data.auth.model.Token
import com.openlysis.data.remote.dto.authentication.AuthTokensDto
import com.openlysis.data.remote.dto.request.SignInRefreshRequest
import com.openlysis.data.remote.dto.request.SignInRequest
import com.openlysis.data.remote.dto.request.SignUpRequest
import com.openlysis.data.remote.jwt.JwtPayloadDecoder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.datetime.Instant
import javax.inject.Inject

/**
 * A JSON Web Token (JWT) implementation of [UserAuthenticator] that uses [AuthenticationApi] to perform
 * user authentication operations such as sign up, sign in and sign-in refresh based on JWTs.
 *
 * @param api The API interface for authentication-related network requests.
 * @param jwtPayloadDecoder The decoder for extracting payload data from JWT access tokens.
 * @param ioDispatcher The coroutine dispatcher used for network operations.
 */
internal class JwtUserAuthenticator
    @Inject
    constructor(
        private val api: AuthenticationApi,
        private val jwtPayloadDecoder: JwtPayloadDecoder,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ) : NetworkApiCaller(ioDispatcher),
        UserAuthenticator {
        override suspend fun signUp(
            email: String,
            password: String
        ) = callApiSafelyWithoutResponse {
            api.signUp(
                SignUpRequest(
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
                        email = email,
                        password = password
                    )
                )

            convertToModelIfSuccess(response) { it.convertToModel() }
        }

        override suspend fun refresh(refreshToken: Token): Outcome<AuthTokens> =
            callApiSafely {
                val response =
                    api.signInRefresh(
                        SignInRefreshRequest(refreshToken.value)
                    )

                convertToModelIfSuccess(response) { it.convertToModel() }
            }

        private fun AuthTokensDto.convertToModel(): AuthTokens {
            val jwtPayload = jwtPayloadDecoder.decode(this.accessToken)
            val accessToken =
                Token(
                    value = this.accessToken,
                    expiresAt = Instant.fromEpochSeconds(jwtPayload.expiration)
                )
            val refreshToken =
                Token(
                    value = this.refreshToken,
                    expiresAt = Instant.parse(this.refreshTokenExpiration)
                )
            return AuthTokens(accessToken, refreshToken)
        }
    }