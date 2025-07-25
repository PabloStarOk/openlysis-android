package com.openlysis.feature.tools.model

/**
 * Represents possible errors when attaching a file.
 *
 * - AlreadyAttached: The file is already attached.
 * - NoData: The file has no data.
 * - TooLarge: The file exceeds the allowed size.
 */
internal enum class AttachedFileError {
    AlreadyAttached,
    NoData,
    TooLarge
}