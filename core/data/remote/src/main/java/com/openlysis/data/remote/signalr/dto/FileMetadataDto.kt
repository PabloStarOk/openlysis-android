package com.openlysis.data.remote.signalr.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlysis.data.analysis.model.analysis.FileMetadata

/**
 * DTO sent over SignalR for [FileMetadata] model.
 *
 * @property name The name of the file.
 * @property contentType The MIME type of the file.
 * @property size The size of the file in bytes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
internal data class FileMetadataDto
    @JsonCreator
    constructor(
        @JsonProperty("Name")
        val name: String,
        @JsonProperty("ContentType")
        val contentType: String,
        @JsonProperty("Size")
        val size: Long
    ) {
        /**
         * Converts this DTO to the domain model [FileMetadata].
         *
         * @return [FileMetadata] instance with the same properties.
         */
        internal fun convertToModel(): FileMetadata = FileMetadata(name, contentType, size)
    }