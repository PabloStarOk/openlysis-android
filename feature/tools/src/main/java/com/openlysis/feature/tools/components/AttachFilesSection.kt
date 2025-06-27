package com.openlysis.feature.tools.components

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.database.getLongOrNull
import com.openlysis.core.designsystem.components.alert.Alert
import com.openlysis.core.designsystem.components.alert.AlertType
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.tools.R
import com.openlysis.feature.tools.data.AttachedFileData
import com.openlysis.feature.tools.data.FileAttachmentSettings

/**
 * Section for attaching files to be analyzed.
 *
 * @param attachedFiles List of files currently attached.
 * @param onFileAttach Callback when a file is attached.
 * @param onFileDetach Callback when a file is detached.
 * @param onFilePasswordChange Callback when a file password is changed.
 * @param enabled Whether file attachment is enabled.
 * @param title The title of the section.
 * @param description The description of the section.
 * @param settings Settings to configure and show file attachment limitations.
 * @param modifier Modifier for styling.
 */
@Composable
internal fun AttachFilesSection(
    attachedFiles: List<AttachedFileData>,
    onFileAttach: (AttachedFileData) -> Unit,
    onFileDetach: (AttachedFileData) -> Unit,
    onFilePasswordChange: (AttachedFileData, String) -> Unit,
    enabled: Boolean,
    title: String,
    description: String,
    settings: FileAttachmentSettings,
    modifier: Modifier = Modifier
) {
    val contentResolver = LocalContext.current.contentResolver
    val getContentContract = remember { ActivityResultContracts.GetContent() }
    val selectFileLauncher =
        rememberLauncherForActivityResult(getContentContract) {
            if (it != null) {
                val fileData = getFileDataFromUri(it, contentResolver)
                onFileAttach(fileData)
            }
        }
    val addButtonType =
        remember(enabled) {
            if (enabled) {
                ButtonType.Positive
            } else {
                ButtonType.PrimaryDisabled
            }
        }

    val fileSizeInMb = settings.maxFileSize.toFloat() / (1024 * 1024)
    val limitMessage =
        stringResource(
            R.string.attach_file_limit_message,
            settings.maxFilesAmount,
            fileSizeInMb
        )

    ToolSection(
        title = title,
        description = description,
        modifier = modifier.animateContentSize()
    ) {
        attachedFiles.forEach {
            AttachedFile(
                onDetachClick = { onFileDetach(it) },
                onPasswordChange = { p -> onFilePasswordChange(it, p) },
                passwordValue = it.password,
                filename = it.displayName
            )
        }

        Alert(
            type = AlertType.Warning,
            text = stringResource(R.string.attach_file_warning_alert)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200)
        ) {
            Text(
                text = limitMessage,
                style = LocalAppTypography.current.bodySmall,
                color = LocalAppColorScheme.current.text.default.secondary
            )

            AppButton(
                type = addButtonType,
                size = SizeType.Default,
                onClick = { selectFileLauncher.launch(settings.mimeTypeFilter) },
                displayLabel = true,
                label = stringResource(R.string.attach_file_button_label),
                displayIcon = true,
                icon = AppIcons.Plus,
                iconAlt = stringResource(R.string.attach_file_button_icon_alt)
            )
        }
    }
}

/**
 * Retrieves file data from the given Uri using the provided ContentResolver.
 *
 * @param fileUri The Uri of the file to extract data from.
 * @param contentResolver The ContentResolver to query file metadata.
 * @return An AttachedFileData object containing the file's Uri, display name, and size.
 */
private fun getFileDataFromUri(
    fileUri: Uri,
    contentResolver: ContentResolver
): AttachedFileData {
    val cursor =
        contentResolver.query(
            fileUri,
            arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
            null,
            null,
            null
        )

    var displayName = ""
    var fileSize: Long = -1
    cursor?.use {
        if (!it.moveToFirst()) {
            return@use
        }

        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        displayName = it.getString(nameIndex)
        fileSize = it.getLongOrNull(sizeIndex) ?: -1
    }

    return AttachedFileData(
        uri = fileUri,
        displayName = displayName,
        size = fileSize
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AttachFilesSectionPreview() {
    OpenlysisTheme(darkTheme = false) {
        AttachFilesSection(
            attachedFiles =
                remember {
                    listOf(
                        AttachedFileData(uri = Uri.EMPTY, displayName = "file-test.pdf", size = 1)
                    )
                },
            onFileAttach = { },
            onFileDetach = { },
            onFilePasswordChange = { _, _ -> },
            enabled = true,
            title = "Test title",
            description = "This is a description",
            settings = FileAttachmentSettings(maxFilesAmount = 1, maxFileSize = 1048576)
        )
    }
}