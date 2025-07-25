package com.openlysis.feature.tools.util

import android.content.Context
import android.text.format.Formatter
import com.openlysis.feature.tools.MessageUiNotifier
import com.openlysis.feature.tools.R
import com.openlysis.feature.tools.data.FileAttachmentSettings
import com.openlysis.feature.tools.model.AttachedFileError

/**
 * Displays a UI message for file attachment errors.
 *
 * @param context The context used for formatting and resources.
 * @param messageUiNotifier Notifier to show messages in the UI.
 * @param error The specific file attachment error encountered.
 * @param attachmentSettings Settings related to file attachments, including max file size.
 */
internal fun showAttachmentErrorUiMessage(
    context: Context,
    messageUiNotifier: MessageUiNotifier,
    error: AttachedFileError,
    attachmentSettings: FileAttachmentSettings
) {
    val errorMessageResId =
        when (error) {
            AttachedFileError.AlreadyAttached -> R.string.info_file_already_attached
            AttachedFileError.NoData -> R.string.error_file_size_zero
            AttachedFileError.TooLarge -> R.string.error_file_too_large
        }

    if (error == AttachedFileError.TooLarge) {
        val formattedFileSize =
            Formatter.formatFileSize(
                context,
                attachmentSettings.maxFileSize
            )
        messageUiNotifier.showMessage(
            errorMessageResId,
            formattedFileSize
        )
    } else {
        messageUiNotifier.showMessage(errorMessageResId)
    }
}