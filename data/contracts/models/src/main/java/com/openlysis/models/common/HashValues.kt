package com.openlysis.models.common

import com.squareup.moshi.JsonClass

/**
 * A collection of cryptographic hash values for a piece of data.
 *
 * @property md5 The MD5 hash of the data, represented as a hexadecimal string.
 * @property sha1 The SHA-1 hash of the data, represented as a hexadecimal string.
 * @property sha256 The SHA-256 hash of the data, represented as a hexadecimal string.
 * @property sha512 The SHA-512 hash of the data, represented as a hexadecimal string.
 */
@JsonClass(generateAdapter = true)
data class HashValues(
    val md5: String,
    val sha1: String,
    val sha256: String,
    val sha512: String
)