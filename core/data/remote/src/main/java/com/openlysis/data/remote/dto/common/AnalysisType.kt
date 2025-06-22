package com.openlysis.data.remote.dto.common

/**
 * Type of an analysis.
 *
 * @property Url Analysis of a URL.
 * @property File Analysis of file.
 * @property Message Analysis of a message.
 */
internal enum class AnalysisType {
    Url,
    File,
    Message
}