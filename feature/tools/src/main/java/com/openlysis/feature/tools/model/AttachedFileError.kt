package com.openlysis.feature.tools.model

/**
 * Represents possible errors when attaching a file.
 *
 * @property AlreadyAttached The file is already attached.
 * @property NoData The file has no data.
 * @property TooLarge The file exceeds the allowed size.
 * @property LimitReached The maximum number of attached files has been reached.
 */
internal enum class AttachedFileError {
    AlreadyAttached,
    NoData,
    TooLarge,
    LimitReached
}