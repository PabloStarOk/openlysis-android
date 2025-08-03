package com.openlysis.data.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.openlysis.core.data.datastore.EncryptedUserAuthData
import com.openlysis.core.data.datastore.UserPreferences
import com.openlysis.data.datastore.cipher.CryptoCipher
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * Serializer implementation for [UserPreferences] proto data.
 * Handles reading and writing [UserPreferences] objects to and from streams.
 */
internal class UserPreferencesSerializer
    @Inject
    constructor() :
    Serializer<UserPreferences> {
        override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): UserPreferences =
            try {
                UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }

        override suspend fun writeTo(
            t: UserPreferences,
            output: OutputStream
        ) {
            t.writeTo(output)
        }
    }