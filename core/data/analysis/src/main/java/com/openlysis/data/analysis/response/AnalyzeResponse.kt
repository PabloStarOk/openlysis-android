package com.openlysis.data.analysis.response

import com.squareup.moshi.JsonClass

/**
 * A response after requesting a analysis of an URL, file or message.
 *
 * @property id ID of the analysis.
 * @property md5 The MD5 hash value associated with the analyzed data.
 * @property sha1 The SHA-1 hash value associated with the analyzed data.
 * @property sha256 The SHA-256 hash value associated with the analyzed data.
 * @property sha512 The SHA-512 hash value associated with the analyzed data.
 */
@JsonClass(generateAdapter = true)
data class AnalyzeResponse(
    val id: String,
    val md5: String,
    val sha1: String,
    val sha256: String,
    val sha512: String
)