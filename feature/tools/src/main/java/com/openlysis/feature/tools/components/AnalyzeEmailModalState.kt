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
 * Remembers and provides an instance of [AnalyzeEmailModalState] for the Analyze Email modal.
 *
 * @param messageUiNotifier A [MessageUiNotifier] to notify message or errors.
 * @param fileAttachmentSettings Configuration for file attachment constraints.
 * @return A remembered [AnalyzeEmailModalState] instance.
 */
@Composable
internal fun rememberAnalyzeEmailModalState(
    messageUiNotifier: MessageUiNotifier,
    fileAttachmentSettings: FileAttachmentSettings
): AnalyzeEmailModalState {
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

    val messageState = rememberSaveable { mutableStateOf(MessageModalSectionState()) }

    return remember {
        AnalyzeEmailModalState(
            snapshotAttachedFiles = snapshotAttachedFiles,
            messageUiNotifier = messageUiNotifier,
            fileAttachmentSettings = fileAttachmentSettings,
            messageState = messageState
        )
    }
}

/**
 * State holder for the Analyze Email modal.
 *
 * @property snapshotAttachedFiles The list of currently attached files, observable for UI updates.
 * @property messageUiNotifier Notifies the UI about messages or errors.
 * @property fileAttachmentSettings Configuration for file attachment constraints.
 * @property messageState State for the message section of the modal.
 */
internal class AnalyzeEmailModalState(
    private val snapshotAttachedFiles: SnapshotStateList<AttachedFileData>,
    private val messageUiNotifier: MessageUiNotifier,
    val fileAttachmentSettings: FileAttachmentSettings,
    val messageState: MutableState<MessageModalSectionState>
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
            val maxInMb = convertFileSizeToMb(fileAttachmentSettings.maxFileSize)
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
     * @param sizeInBytes The file size in bytes.
     * @return The file size in megabytes as a Float.
     */
    private fun convertFileSizeToMb(sizeInBytes: Long): Float =
        fileAttachmentSettings.maxFileSize.toFloat() / (1024f * 1024f)
}