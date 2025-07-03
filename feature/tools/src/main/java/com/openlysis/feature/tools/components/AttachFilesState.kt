package com.openlysis.feature.tools.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
 * Creates and remembers the state for file attachments in a composable context.
 *
 * @param settings Configuration parameters for file attachments including size limits and maximum count
 * @param messageUiNotifier Handler for displaying UI messages and notifications to the user
 * @return A new or existing [AttachFilesState] instance that manages file attachments
 */
@Composable
internal fun rememberAttachFilesState(
    settings: FileAttachmentSettings,
    messageUiNotifier: MessageUiNotifier
): AttachFilesState {
    val attachedFiles =
        rememberSaveable(
            saver =
                listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() }
                )
        ) {
            mutableStateListOf<AttachedFileData>()
        }

    return remember {
        AttachFilesState(
            _attachedFiles = attachedFiles,
            settings = settings,
            messageUiNotifier = messageUiNotifier
        )
    }
}

/**
 * Manages the state of file attachments.
 *
 * @property _attachedFiles A mutable snapshot list to store the attached files.
 * @property settings Configuration for file attachment constraints
 * @property messageUiNotifier Handler for displaying UI messages to the user
 */
internal class AttachFilesState(
    private val _attachedFiles: SnapshotStateList<AttachedFileData>,
    private val settings: FileAttachmentSettings,
    private val messageUiNotifier: MessageUiNotifier
) {
    val attachedFiles: List<AttachedFileData> = _attachedFiles
    val attachmentEnabled: Boolean
        get() = _attachedFiles.size < settings.maxFilesAmount

    /**
     * Attaches a file to the list if it meets the specified criteria.
     *
     * @param file The file data to be attached
     * @throws Nothing The function handles all error cases internally through UI messages
     */
    fun attachFile(file: AttachedFileData) {
        if (_attachedFiles.any { f -> f.uri == file.uri }) {
            messageUiNotifier.showMessage(R.string.info_file_already_attached)
            return
        }

        if (file.size < 1) {
            messageUiNotifier.showMessage(R.string.error_file_size_zero)
            return
        }

        if (file.size > settings.maxFileSize) {
            val maxInMb = convertFileSizeToMb()
            val mbUnit = "MB"
            messageUiNotifier.showMessage(R.string.error_file_too_large, maxInMb, mbUnit)
            return
        }

        _attachedFiles.add(file)
    }

    /**
     * Updates the password for a specific attached file.
     *
     * @param file The file whose password needs to be updated
     * @param password The new password to be set for the file
     */
    fun updateFilePassword(
        file: AttachedFileData,
        password: String
    ) {
        var index = _attachedFiles.indexOfFirst { it.uri == file.uri }
        if (index > -1) {
            _attachedFiles[index] = _attachedFiles[index].copy(password = password)
            return
        }
    }

    /**
     * Removes a file from the list of attached files.
     *
     * @param file The file data to be detached
     */
    fun detachFile(file: AttachedFileData) {
        var file = _attachedFiles.find { it.uri == file.uri }
        _attachedFiles.remove(file)
    }

    /**
     * Converts the maximum file size setting from bytes to megabytes.
     *
     * @return The maximum file size in megabytes as a Float value
     */
    private fun convertFileSizeToMb(): Float = settings.maxFileSize.toFloat() / (1024f * 1024f)
}