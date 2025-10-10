package com.openlysis.data.attachment

import android.net.Uri
import com.openlysis.core.outcome.AppError
import java.io.FileNotFoundException

/**
 * Represents various errors that can occur during the attachment creation process.
 *
 * @property uri The URI of the file that caused the attachment creation to fail.
 */
sealed class AttachmentCreationError(
    open val uri: Uri
) : AppError {
    /**
     * Error indicating that the input stream for reading the file content is null.
     *
     * @property uri The URI of the file that could not be opened for reading.
     */
    data class NullInputStream(
        override val uri: Uri
    ) : AttachmentCreationError(uri)

    /**
     * Error indicating that the file metadata cursor is null.
     *
     * @property uri The URI of the file whose metadata could not be retrieved.
     */
    data class NullMetadataCursor(
        override val uri: Uri
    ) : AttachmentCreationError(uri)

    /**
     * Error indicating that the file metadata cursor is empty.
     *
     * @property uri The URI of the file whose metadata cursor is empty.
     */
    data class EmptyMetadataCursor(
        override val uri: Uri
    ) : AttachmentCreationError(uri)

    /**
     * Error indicating that the specified file could not be found in the filesystem.
     *
     * @property uri The URI of the file that could not be found.
     * @property exception The underlying FileNotFoundException that was thrown.
     */
    data class FileNotFound(
        override val uri: Uri,
        val exception: FileNotFoundException
    ) : AttachmentCreationError(uri)
}