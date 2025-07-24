package com.openlysis.data.auth

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Default implementation of [UserAuthDataRepository] that delegates to [UserAuthLocalDataSource].
 *
 * @property localDataSource The local data source for user authentication data.
 */
internal class DefaultUserAuthDataRepository
    @Inject
    constructor(
        private val localDataSource: UserAuthLocalDataSource
    ) : UserAuthDataRepository {
        override val data: Flow<UserAuthData> = localDataSource.data

        override suspend fun setSignedIn(signedIn: Boolean) = localDataSource.setSignedIn(signedIn)

        override suspend fun saveApiKey(apiKey: String) = localDataSource.saveApiKey(apiKey)

        override suspend fun deleteApiKey() = localDataSource.deleteApiKey()
    }