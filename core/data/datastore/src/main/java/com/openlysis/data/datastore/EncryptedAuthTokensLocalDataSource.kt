package com.openlysis.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.openlysis.core.data.datastore.EncryptedAuthTokens
import com.openlysis.data.auth.AuthTokensLocalDataSource
import com.openlysis.data.auth.model.AuthTokens
import com.openlysis.data.auth.model.Token
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import okio.IOException
import javax.inject.Inject
import com.openlysis.core.data.datastore.Token as ProtoToken

/**
 * Local data source which encrypts and saves the [AuthTokens].
 *
 * @property authTokensDataStore DataStore instance holding [EncryptedAuthTokens].
 * @constructor Injects the required DataStore dependency.
 */
internal class EncryptedAuthTokensLocalDataSource
    @Inject
    constructor(
        private val authTokensDataStore: DataStore<EncryptedAuthTokens>
    ) : AuthTokensLocalDataSource {
        override val data =
            authTokensDataStore.data.map {
                AuthTokens(
                    accessToken = it.accessToken.parseProtoToken(),
                    refreshToken = it.refreshToken.parseProtoToken()
                )
            }

        override suspend fun saveTokens(authTokens: AuthTokens) {
            val accessToken = authTokens.accessToken
            if (accessToken == null) {
                throw IllegalArgumentException("Access token must not be null")
            }

            val refreshToken = authTokens.refreshToken
            if (refreshToken == null) {
                throw IllegalArgumentException("Refresh token must not be null")
            }

            try {
                authTokensDataStore.updateData {
                    it
                        .toBuilder()
                        .setAccessToken(accessToken.convertToProtoToken())
                        .setRefreshToken(refreshToken.convertToProtoToken())
                        .build()
                }
            } catch (ioException: IOException) {
                Log.e(
                    "EncryptedAuthToken",
                    "Failed to save authentication tokens.",
                    ioException
                )
            }
        }

        override suspend fun deleteTokens() {
            try {
                authTokensDataStore.updateData {
                    it
                        .toBuilder()
                        .clearAccessToken()
                        .clearRefreshToken()
                        .build()
                }
            } catch (ioException: IOException) {
                Log.e(
                    "EncryptedAuthToken",
                    "Failed to delete authentication tokens.",
                    ioException
                )
            }
        }

        private fun ProtoToken?.parseProtoToken(): Token? {
            if (this == null) return null

            val expiresAt = Instant.fromEpochSeconds(this.expiresAtSeconds)
            return Token(
                value = this.value,
                expiresAt = expiresAt
            )
        }

        private fun Token.convertToProtoToken(): ProtoToken =
            ProtoToken
                .newBuilder()
                .setValue(this.value)
                .setExpiresAtSeconds(this.expiresAt.epochSeconds)
                .build()
    }