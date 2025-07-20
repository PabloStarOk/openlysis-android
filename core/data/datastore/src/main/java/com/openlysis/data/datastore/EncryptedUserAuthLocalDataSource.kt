package com.openlysis.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.openlysis.core.data.datastore.EncryptedUserAuthData
import com.openlysis.data.auth.UserAuthData
import com.openlysis.data.auth.UserAuthLocalDataSource
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

/**
 * Local data source which encrypts the [UserAuthData.apiKey] and saves the [UserAuthData].
 *
 * @property userAuthDataStore DataStore instance holding [EncryptedUserAuthData].
 * @constructor Injects the required DataStore dependency.
 */
internal class EncryptedUserAuthLocalDataSource
    @Inject
    constructor(
        private val userAuthDataStore: DataStore<EncryptedUserAuthData>
    ) : UserAuthLocalDataSource {
        override val data =
            userAuthDataStore.data.map {
                UserAuthData(
                    isSignedIn = it.isSignedIn,
                    apiKey = it.apiKey
                )
            }

        override suspend fun setSignedIn(signedIn: Boolean) {
            try {
                userAuthDataStore.updateData {
                    it
                        .toBuilder()
                        .setIsSignedIn(signedIn)
                        .build()
                }
            } catch (ioException: IOException) {
                Log.e("EncryptedUserAuthData", "Failed to set value for isSignedIn", ioException)
            }
        }

        override suspend fun saveApiKey(apiKey: String) {
            if (apiKey.isEmpty()) {
                throw IllegalArgumentException("API Key must not be empty.")
            }

            try {
                userAuthDataStore.updateData {
                    it
                        .toBuilder()
                        .setApiKey(apiKey)
                        .build()
                }
            } catch (ioException: IOException) {
                Log.e("EncryptedUserAuthData", "Failed to set API Key", ioException)
            }
        }

        override suspend fun deleteApiKey() {
            try {
                userAuthDataStore.updateData {
                    it
                        .toBuilder()
                        .clearApiKey()
                        .build()
                }
            } catch (ioException: IOException) {
                Log.e("EncryptedUserAuthData", "Failed to remove API Key", ioException)
            }
        }
    }