package com.openlysis.data.attachment

import com.openlysis.data.analysis.model.common.AnalysisError
import java.io.FileNotFoundException

/**
 * Represents various errors that can occur during attachment creation process.
 */
sealed interface AttachmentCreationError : AnalysisError {
    /**
     * Error indicating that the input stream provided is null.
     */
    data object NullInputStream : AttachmentCreationError

    /**
     * Error indicating that the file metadata cursor is null, which does not allow to retrieve the required information to analyze the file.
     */
    data object NullMetadataCursor : AttachmentCreationError

    /**
     * Error indicating that the file metadata cursor is empty, which does not allow to retrieve the required information to analyze the file.
     */
    data object EmptyMetadataCursor : AttachmentCreationError

    /**
     * Error indicating that the specified file could not be found in the filesystem.
     *
     * @property exception The underlying FileNotFound exception that was thrown
     */
    data class FileNotFound(
        val exception: FileNotFoundException
    ) : AttachmentCreationError
}