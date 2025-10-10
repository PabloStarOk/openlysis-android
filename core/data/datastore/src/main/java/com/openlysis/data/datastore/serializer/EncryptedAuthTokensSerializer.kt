package com.openlysis.data.datastore.serializer

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.openlysis.core.data.datastore.EncryptedAuthTokens
import com.openlysis.data.datastore.cipher.CryptoCipher
import com.openlysis.data.datastore.cipher.DecryptionException
import jakarta.inject.Inject
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer implementation for [EncryptedAuthTokens].
 * Handles reading and writing of [EncryptedAuthTokens] using protocol buffers.
 * Data is encrypted and decrypted using [CryptoCipher] before serialization and after deserialization.
 */
internal class EncryptedAuthTokensSerializer
    @Inject
    constructor(
        private val cryptoCipher: CryptoCipher
    ) : Serializer<EncryptedAuthTokens> {
        override val defaultValue: EncryptedAuthTokens =
            EncryptedAuthTokens
                .getDefaultInstance()

        override suspend fun readFrom(input: InputStream): EncryptedAuthTokens {
            val encryptedBytes = input.use { it.readBytes() }

            if (encryptedBytes.isEmpty()) {
                return defaultValue
            }

            try {
                val decryptedBytes = cryptoCipher.decrypt(encryptedBytes)
                return EncryptedAuthTokens.parseFrom(decryptedBytes)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            } catch (exception: DecryptionException) {
                val errorMessage = "Encrypted authentication tokens tampered."
                Log.e("AuthTokensSerializer", errorMessage, exception)
                throw CorruptionException(errorMessage, exception)
            }
        }

        override suspend fun writeTo(
            t: EncryptedAuthTokens,
            output: OutputStream
        ) {
            val plainBytes = t.toByteArray()
            val encryptedBytes = cryptoCipher.encrypt(plainBytes)
            output.use { it.write(encryptedBytes) }
        }
    }