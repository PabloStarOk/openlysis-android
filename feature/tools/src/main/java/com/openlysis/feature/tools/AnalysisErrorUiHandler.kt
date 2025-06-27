package com.openlysis.feature.tools

import android.util.Log
import com.openlysis.data.analysis.core.error.RepositoryError
import com.openlysis.data.analysis.model.common.AnalysisError
import com.openlysis.data.attachment.AttachmentCreationError

/**
 * Handles UI-related error processing for analysis operations.
 * This class is responsible for converting various error types into user-friendly messages
 * and logging appropriate error information.
 *
 * @property messageUiNotifier The notifier component used to display messages to the user interface
 */
internal class AnalysisErrorUiHandler(
    private val messageUiNotifier: MessageUiNotifier
) {
    /**
     * Handles [AnalysisError] objects.
     *
     * @param error The analysis error to be handled.
     */
    fun handle(error: AnalysisError) {
        when (error) {
            is RepositoryError -> handleRepositoryError(messageUiNotifier, error)
            is AttachmentCreationError -> handleAttachmentCreationError(messageUiNotifier, error)
        }
    }

    /**
     * Handles repository-related errors by displaying appropriate error messages to the user
     * and logging the error details.
     *
     * @param messageUiNotifier The notifier used to display error messages to the user
     * @param error The specific repository error that occurred
     */
    private fun handleRepositoryError(
        messageUiNotifier: MessageUiNotifier,
        error: RepositoryError
    ) {
        val messageRes =
            when (error) {
                is RepositoryError.BadRequest -> R.string.error_analysis_repository_bad_request
                RepositoryError.AccessDenied -> R.string.error_analysis_repository_generic
                RepositoryError.Network -> R.string.error_analysis_repository_network
                RepositoryError.NotFound -> R.string.error_analysis_repository_generic
                RepositoryError.OperationCanceled ->
                    R.string.error_analysis_repository_operation_canceled
                RepositoryError.Server -> R.string.error_analysis_repository_server
                RepositoryError.ServerUnreachable ->
                    R.string.error_analysis_repository_unreachable_server
                RepositoryError.Unavailable -> R.string.error_analysis_repository_unavailable
                RepositoryError.Unknown -> R.string.error_analysis_repository_unknown
            }
        messageUiNotifier.showMessage(messageRes)

        if (Log.isLoggable(REPOSITORY_ERROR_LOG_TAG, Log.ERROR)) {
            Log.e(REPOSITORY_ERROR_LOG_TAG, "Repository error occurred: $error")
        }
    }

    /**
     * Handles errors that occur during attachment file creation.
     *
     * @param messageUiNotifier The notifier used to display error messages to the user
     * @param error The specific attachment creation error that occurred
     */
    private fun handleAttachmentCreationError(
        messageUiNotifier: MessageUiNotifier,
        error: AttachmentCreationError
    ) {
        messageUiNotifier.showMessage(R.string.error_attachment_creation)
        if (!Log.isLoggable(ATTACHMENT_ERROR_LOG_TAG, Log.ERROR)) {
            return
        }

        when (error) {
            is AttachmentCreationError.NullInputStream ->
                Log.e(ATTACHMENT_ERROR_LOG_TAG, "Null input stream for Android URI: ${error.uri}")
            is AttachmentCreationError.NullMetadataCursor ->
                Log.e(
                    ATTACHMENT_ERROR_LOG_TAG,
                    "Null metadata cursor for Android URI: ${error.uri}"
                )
            is AttachmentCreationError.EmptyMetadataCursor ->
                Log.e(
                    ATTACHMENT_ERROR_LOG_TAG,
                    "Empty metadata cursor for Android URI: ${error.uri}"
                )
            is AttachmentCreationError.FileNotFound ->
                Log.e(
                    ATTACHMENT_ERROR_LOG_TAG,
                    "File not found for Android URI: ${error.uri}",
                    error.exception
                )
        }
    }
}

/** Tag used for logging repository-related errors in LogCat. */
private const val REPOSITORY_ERROR_LOG_TAG = "RepositoryError"

/** Tag used for logging attachment creation errors in LogCat. */
private const val ATTACHMENT_ERROR_LOG_TAG = "AttachmentCreationError"