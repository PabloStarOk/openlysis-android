package com.openlysis.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.openlysis.core.data.datastore.EncryptedUserAuthData
import jakarta.inject.Inject
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer implementation for [EncryptedUserAuthData].
 * Handles reading and writing of [EncryptedUserAuthData] using protocol buffers.
 */
internal class EncryptedUserAuthDataSerializer
    @Inject
    constructor() :
    Serializer<EncryptedUserAuthData> {
        override val defaultValue: EncryptedUserAuthData =
            EncryptedUserAuthData
                .getDefaultInstance()

        override suspend fun readFrom(input: InputStream): EncryptedUserAuthData {
            try {
                return EncryptedUserAuthData.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(
            t: EncryptedUserAuthData,
            output: OutputStream
        ) = t.writeTo(output)
    }