package com.openlysis.feature.tools.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.openlysis.feature.tools.MessageUiNotifier
import com.openlysis.feature.tools.R
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings

/**
 * Creates and remembers an [EmailAnalysisState] instance that manages email analysis functionality.
 *
 * @param messageUiNotifier Handler for displaying UI messages and notifications
 * @param fileAttachmentSettings Configuration settings for file attachment restrictions
 * @return A new or existing [EmailAnalysisState] instance
 */
@Composable
internal fun rememberEmailAnalysisState(
    messageUiNotifier: MessageUiNotifier,
    fileAttachmentSettings: FileAttachmentSettings
): EmailAnalysisState {
    val snapshotAttachedFiles =
        rememberSaveable(
            saver =
                listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
        ) {
            mutableStateListOf<AttachedFileData>()
        }

    val messageState = rememberSaveable { mutableStateOf(MessageSectionState()) }

    return remember {
        EmailAnalysisState(
            snapshotAttachedFiles = snapshotAttachedFiles,
            messageUiNotifier = messageUiNotifier,
            fileAttachmentSettings = fileAttachmentSettings,
            messageState = messageState
        )
    }
}

/**
 * Manages the state for email analysis functionality, including attached files and messaging.
 *
 * @property snapshotAttachedFiles List of currently attached files that maintains state across recomposition
 * @property messageUiNotifier Handler for displaying UI messages and notifications
 * @property fileAttachmentSettings Configuration for file attachment restrictions
 * @property messageState Current state of the message section UI
 */
internal class EmailAnalysisState(
    private val snapshotAttachedFiles: SnapshotStateList<AttachedFileData>,
    private val messageUiNotifier: MessageUiNotifier,
    val fileAttachmentSettings: FileAttachmentSettings,
    val messageState: MutableState<MessageSectionState>
) {
    val attachedFiles: List<AttachedFileData>
        get() = snapshotAttachedFiles

    /**
     * Adds a new attached file to the list if it passes validation checks.
     *
     * Checks for duplicate files, zero file size, and file size exceeding the allowed maximum.
     * Notifies the user via [messageUiNotifier] if any validation fails.
     *
     * @param file The [AttachedFileData] to add.
     */
    fun addAttachedFile(file: AttachedFileData) {
        if (snapshotAttachedFiles.any { f -> f.uri == file.uri }) {
            messageUiNotifier.showMessage(R.string.info_file_already_attached)
            return
        }

        if (file.size < 1) {
            messageUiNotifier.showMessage(R.string.error_file_size_zero)
            return
        }

        if (file.size > fileAttachmentSettings.maxFileSize) {
            val maxInMb = convertFileSizeToMb()
            val mbUnit = "MB"
            messageUiNotifier.showMessage(R.string.error_file_too_large, maxInMb, mbUnit)
            return
        }

        snapshotAttachedFiles.add(file)
    }

    /**
     * Updates the password for an attached file identified by its URI.
     *
     * @param file The [AttachedFileData] whose password should be updated.
     * @param password The new password to set for the file.
     */
    fun updateAttachedFilePassword(
        file: AttachedFileData,
        password: String
    ) {
        var index = snapshotAttachedFiles.indexOfFirst { it.uri == file.uri }
        if (index > -1) {
            snapshotAttachedFiles[index] = snapshotAttachedFiles[index].copy(password = password)
            return
        }
    }

    /**
     * Removes an attached file from the list by matching its URI.
     *
     * @param file The [AttachedFileData] to remove from the attached files list.
     */
    fun removeAttachedFile(file: AttachedFileData) {
        var file = snapshotAttachedFiles.find { it.uri == file.uri }
        snapshotAttachedFiles.remove(file)
    }

    /**
     * Converts a file size in bytes to megabytes (MB).
     *
     * @return The file size in megabytes as a Float.
     */
    private fun convertFileSizeToMb(): Float =
        fileAttachmentSettings.maxFileSize.toFloat() / (1024f * 1024f)
}