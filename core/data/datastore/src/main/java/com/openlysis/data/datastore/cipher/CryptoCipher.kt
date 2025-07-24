package com.openlysis.data.datastore.cipher

import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.data.datastore.constant.EncryptionParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

/**
 * A cipher that provides encryption and decryption functionality using AES-GCM.
 *
 * @property defaultDispatcher Coroutine dispatcher for running cryptographic operations.
 * @property keyProvider Provider for cryptographic keys.
 */
internal class CryptoCipher
    @Inject
    constructor(
        @Dispatcher(AppDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher,
        private val keyProvider: CipherKeyProvider
    ) {
        private val ivSizeOffset = 1
        private val authTagLength = 128

        suspend fun encrypt(plainBytes: ByteArray): ByteArray =
            withContext(defaultDispatcher) {
                val key = keyProvider.getKey()
                val cipher = Cipher.getInstance(EncryptionParams.TRANSFORMATION)
                cipher.init(Cipher.ENCRYPT_MODE, key)
                val encryptedBytes = cipher.doFinal(plainBytes)
                generateIvWithEncryptedBytes(cipher.iv, encryptedBytes)
            }

        suspend fun decrypt(encryptedBytes: ByteArray): ByteArray =
            withContext(defaultDispatcher) {
                if (encryptedBytes.isEmpty()) {
                    throw IllegalArgumentException("Cannot decrypt an empty byte array.")
                }

                val key = keyProvider.getKey()

                try {
                    val cipher = Cipher.getInstance(EncryptionParams.TRANSFORMATION)
                    val ivSize = getIvSize(encryptedBytes)
                    val iv = encryptedBytes.copyOfRange(ivSizeOffset, ivSize + ivSizeOffset)
                    val encryptedData =
                        encryptedBytes.copyOfRange(
                            ivSize + ivSizeOffset,
                            encryptedBytes.size
                        )
                    val spec = GCMParameterSpec(authTagLength, iv)
                    cipher.init(Cipher.DECRYPT_MODE, key, spec)
                    cipher.doFinal(encryptedData)
                } catch (exception: IndexOutOfBoundsException) {
                    throw DecryptionException("Decryption failed due to malformed data.", exception)
                } catch (exception: GeneralSecurityException) {
                    throw DecryptionException(
                        "Decryption failed due to a security error.",
                        exception
                    )
                }
            }

        private fun generateIvWithEncryptedBytes(
            iv: ByteArray,
            encryptedBytes: ByteArray
        ): ByteArray = byteArrayOf(iv.size.toByte()) + iv + encryptedBytes

        private fun getIvSize(input: ByteArray): Int = input.first().toInt()
    }