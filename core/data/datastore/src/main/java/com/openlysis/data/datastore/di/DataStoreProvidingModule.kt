package com.openlysis.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.openlysis.core.data.datastore.EncryptedAuthTokens
import com.openlysis.core.data.datastore.UserPreferences
import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.ApplicationScope
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.data.datastore.cipher.CipherKeyProvider
import com.openlysis.data.datastore.constant.EncryptionParams
import com.openlysis.data.datastore.constant.ProtoFileNames
import com.openlysis.data.datastore.serializer.EncryptedAuthTokensSerializer
import com.openlysis.data.datastore.serializer.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import java.security.KeyStore
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing DataStore-related dependencies.
 * Installed in the SingletonComponent to ensure single instance across the application.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreProvidingModule {
    @Singleton
    @Provides
    fun provideEncryptedUserAuthDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        serializer: EncryptedAuthTokensSerializer
    ): DataStore<EncryptedAuthTokens> =
        DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            corruptionHandler =
                ReplaceFileCorruptionHandler<EncryptedAuthTokens>(
                    produceNewData = { EncryptedAuthTokens.getDefaultInstance() }
                )
        ) {
            context.dataStoreFile(ProtoFileNames.ENCRYPTED_USER_AUTH_DATA)
        }

    @Singleton
    @Provides
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        serializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            corruptionHandler =
                ReplaceFileCorruptionHandler<UserPreferences>(
                    produceNewData = { UserPreferences.getDefaultInstance() }
                )
        ) {
            context.dataStoreFile(ProtoFileNames.USER_PREFERENCES)
        }

    @Singleton
    @Provides
    fun provideCipherKeyProvider(
        @Dispatcher(AppDispatcher.Default) defaultDispatcher: CoroutineDispatcher
    ): CipherKeyProvider =
        CipherKeyProvider(
            dispatcher = defaultDispatcher,
            keyStore =
                KeyStore
                    .getInstance(
                        EncryptionParams.PROVIDER
                    ).apply { this.load(null) },
            keyAlias = EncryptionParams.KEY_ALIAS,
            keyPassword = null,
            keySize = EncryptionParams.KEY_SIZE
        )
}