package com.openlysis.data.user

import com.openlysis.data.user.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Default implementation of [UserDataRepository] that interacts with a local data source.
 *
 * @property localDataSource The local data source for user data operations.
 */
internal class DefaultUserDataRepository
    @Inject
    constructor(
        private val localDataSource: UserDataLocalDataSource
    ) : UserDataRepository {
        override val data: Flow<UserData> = localDataSource.data

        override suspend fun addOrUpdatedAskedPermission(
            permission: String,
            granted: Boolean
        ) {
            localDataSource.addOrUpdateAskedPermission(permission, granted)
        }
    }