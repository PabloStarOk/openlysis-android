package com.openlysis.data.remote

import com.openlysis.data.remote.constant.ApiEndpoints
import com.openlysis.data.remote.dto.authentication.AuthTokensDto
import com.openlysis.data.remote.dto.request.SignInRefreshRequest
import com.openlysis.data.remote.dto.request.SignInRequest
import com.openlysis.data.remote.dto.request.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API interface for authentication-related endpoints.
 */
internal interface AuthenticationApi {
    /**
     * Registers a new user.
     *
     * @param request The sign-up request body containing user details.
     * @return A [Response] with no content if successful.
     */
    @POST(ApiEndpoints.SIGN_UP)
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<Unit>

    /**
     * Authenticates a user and returns a sign-in response.
     *
     * @param request The sign-in request body containing credentials.
     * @return A [Response] containing [AuthTokensDto] if successful.
     */
    @POST(ApiEndpoints.SIGN_IN)
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<AuthTokensDto>

    /**
     * Refreshes authentication tokens using a refresh token.
     *
     * @param refreshToken The request body containing the refresh token.
     * @return A [Response] containing new [AuthTokensDto] if successful.
     */
    @POST(ApiEndpoints.SIGN_IN_REFRESH)
    suspend fun signInRefresh(
        @Body refreshToken: SignInRefreshRequest
    ): Response<AuthTokensDto>
}