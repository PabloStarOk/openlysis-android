package com.openlysis.data.datastore.cipher

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.openlysis.data.datastore.constant.EncryptionParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.security.Key
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.inject.Inject

/**
 * Provides cryptographic key management using Android Keystore.
 *
 * @param dispatcher Coroutine dispatcher for background operations.
 * @param keyStore Instance of the Android KeyStore.
 * @param keyAlias Alias for the key in the KeyStore.
 * @param keyPassword Optional password for the key in the KeyStore.
 * @param keySize Size of the key to generate.
 */
internal class CipherKeyProvider
    @Inject
    constructor(
        private val dispatcher: CoroutineDispatcher,
        private val keyStore: KeyStore,
        private val keyAlias: String,
        private val keyPassword: CharArray?,
        private val keySize: Int
    ) {
        /**
         * Retrieves the cryptographic key from the Android KeyStore.
         * If the key does not exist, it generates a new one.
         *
         * @return A [Key] for encryption and decryption operations.
         */
        suspend fun getKey(): Key =
            if (keyStore.containsAlias(keyAlias)) {
                keyStore.getKey(keyAlias, keyPassword)
            } else {
                generateKey()
            }

        private suspend fun generateKey(): Key =
            withContext(dispatcher) {
                val keyGenerator =
                    KeyGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES,
                        EncryptionParams.PROVIDER
                    )
                val keySpec =
                    KeyGenParameterSpec
                        .Builder(
                            keyAlias,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                        ).apply {
                            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            setKeySize(keySize)
                            setRandomizedEncryptionRequired(true)
                        }.build()

                keyGenerator.init(keySpec)
                keyGenerator.generateKey()
            }
    }