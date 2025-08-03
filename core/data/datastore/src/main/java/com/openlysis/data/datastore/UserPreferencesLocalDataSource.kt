package com.openlysis.data.datastore

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.datastore.core.DataStore
import com.openlysis.core.data.datastore.UserPreferences
import com.openlysis.data.user.UserDataLocalDataSource
import com.openlysis.data.user.model.Permission
import com.openlysis.data.user.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Local data source for managing user preferences using DataStore.
 *
 * @property context The application context, injected by Hilt.
 * @property userPreferencesDataStore The DataStore instance for UserPreferences.
 */
internal class UserPreferencesLocalDataSource
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val userPreferencesDataStore: DataStore<UserPreferences>
    ) : UserDataLocalDataSource {
        override val data: Flow<UserData> =
            userPreferencesDataStore.data.map {
                val permissions =
                    it.askedPermissionsMap.map { permission ->
                        val isGrantedFromSystem =
                            context.checkSelfPermission(permission.key) ==
                                PackageManager.PERMISSION_GRANTED

                        Permission(
                            manifestName = permission.key,
                            wasGrantedFromApp = permission.value,
                            isGrantedFromSystem = isGrantedFromSystem
                        )
                    }
                UserData(askedPermissions = permissions)
            }

        override suspend fun addOrUpdateAskedPermission(
            permission: String,
            granted: Boolean
        ) {
            try {
                userPreferencesDataStore.updateData {
                    it
                        .toBuilder()
                        .putAskedPermissions(permission, granted)
                        .build()
                }
            } catch (ioException: IOException) {
                Log.e("UserPreferences", "Failed to update permission.", ioException)
            }
        }
    }