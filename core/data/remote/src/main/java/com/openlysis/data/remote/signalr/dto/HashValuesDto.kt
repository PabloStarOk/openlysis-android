package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.common.HashValues

/**
 * DTO sent over SignalR for [HashValues] model.
 *
 * @property md5 The MD5 hash value.
 * @property sha1 The SHA-1 hash value.
 * @property sha256 The SHA-256 hash value.
 * @property sha512 The SHA-512 hash value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class HashValuesDto
    @JsonCreator
    constructor(
        @JsonProperty("Md5")
        val md5: String,
        @JsonProperty("Sha1")
        val sha1: String,
        @JsonProperty("Sha256")
        val sha256: String,
        @JsonProperty("Sha512")
        val sha512: String
    ) {
        /**
         * Converts this DTO to the domain model [HashValues].
         *
         * @return [HashValues] domain model instance.
         */
        internal fun convertToModel(): HashValues = HashValues(md5, sha1, sha256, sha512)
    }