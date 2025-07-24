package com.openlysis.data.datastore

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.openlysis.core.data.datastore.EncryptedUserAuthData
import com.openlysis.data.datastore.cipher.CryptoCipher
import com.openlysis.data.datastore.cipher.DecryptionException
import jakarta.inject.Inject
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer implementation for [EncryptedUserAuthData].
 * Handles reading and writing of [EncryptedUserAuthData] using protocol buffers.
 * Data is encrypted and decrypted using [CryptoCipher] before serialization and after deserialization.
 */
internal class EncryptedUserAuthDataSerializer
    @Inject
    constructor(
        private val cryptoCipher: CryptoCipher
    ) : Serializer<EncryptedUserAuthData> {
        override val defaultValue: EncryptedUserAuthData =
            EncryptedUserAuthData
                .getDefaultInstance()

        override suspend fun readFrom(input: InputStream): EncryptedUserAuthData {
            val encryptedBytes = input.use { it.readBytes() }

            if (encryptedBytes.isEmpty()) {
                return defaultValue
            }

            try {
                val decryptedBytes = cryptoCipher.decrypt(encryptedBytes)
                return EncryptedUserAuthData.parseFrom(decryptedBytes)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            } catch (exception: DecryptionException) {
                Log.e(
                    "UserAuthDataSerializer",
                    "Encrypted user authentication data tampered.",
                    exception
                )
                throw CorruptionException("Encrypted user authentication data tampered.", exception)
            }
        }

        override suspend fun writeTo(
            t: EncryptedUserAuthData,
            output: OutputStream
        ) {
            val plainBytes = t.toByteArray()
            val encryptedBytes = cryptoCipher.encrypt(plainBytes)
            output.use { it.write(encryptedBytes) }
        }
    }