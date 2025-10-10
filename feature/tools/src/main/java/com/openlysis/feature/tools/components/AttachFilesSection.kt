package com.openlysis.feature.tools.components

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.openlysis.core.designsystem.components.TextInput
import com.openlysis.core.designsystem.components.alert.Alert
import com.openlysis.core.designsystem.components.alert.AlertType
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.tools.R
import com.openlysis.feature.tools.model.AttachedFileData
import com.openlysis.feature.tools.model.FileAttachmentSettings
import kotlin.random.Random

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
 * @param pickMultipleFiles If to allow the user to pick multiple files when browsing the files to be attached.
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
    pickMultipleFiles: Boolean,
    modifier: Modifier = Modifier
) {
    val contentResolver = LocalContext.current.contentResolver
    val launcherResultHandler =
        if (pickMultipleFiles) {
            val contract = remember { ActivityResultContracts.GetMultipleContents() }
            rememberLauncherForActivityResult(contract) {
                handleMultipleContentsContract(
                    uris = it,
                    contentResolver = contentResolver,
                    onFileAttach = onFileAttach
                )
            }
        } else {
            val contract = remember { ActivityResultContracts.GetContent() }
            rememberLauncherForActivityResult(contract) {
                handleContentContract(
                    uri = it,
                    contentResolver = contentResolver,
                    onFileAttach = onFileAttach
                )
            }
        }

    var showEditPasswordDialog by rememberSaveable { mutableStateOf(false) }
    var editPasswordTargetFile by rememberSaveable { mutableStateOf<AttachedFileData?>(null) }

    ToolSection(
        title = title,
        description = description,
        modifier = modifier.animateContentSize()
    ) {
        attachedFiles.forEach {
            AttachedFile(
                onSetPasswordRequest = {
                    editPasswordTargetFile = it
                    showEditPasswordDialog = true
                },
                onDetachClick = { onFileDetach(it) },
                password = it.password,
                filename = it.displayName,
                attachedFileError = it.error
            )
        }

        Alert(
            type = AlertType.Warning,
            text = stringResource(R.string.attach_file_warning_alert)
        )

        AttachFileButton(
            onClick = { launcherResultHandler.launch(settings.mimeTypeFilter) },
            maxFileSize = settings.maxFileSize,
            maxFilesAmount = settings.maxFilesAmount,
            enabled = enabled,
            disabledMessageLabel = stringResource(R.string.attach_file_button_disabled_label),
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showEditPasswordDialog && editPasswordTargetFile != null) {
        val file = editPasswordTargetFile as AttachedFileData
        EditPasswordDialog(
            onApplyRequest = { newPasswd ->
                onFilePasswordChange(file, newPasswd)
                editPasswordTargetFile = null
                showEditPasswordDialog = false
            },
            onDismissRequest = {
                editPasswordTargetFile = null
                showEditPasswordDialog = false
            },
            filename = file.displayName,
            currentPassword = file.password
        )
    }
}

@Composable
private fun AttachFileButton(
    onClick: () -> Unit,
    maxFileSize: Long,
    maxFilesAmount: Int,
    enabled: Boolean,
    disabledMessageLabel: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication =
        ripple(bounded = true, color = LocalAppColorScheme.current.background.brand.primary)
    val label =
        if (enabled) {
            stringResource(R.string.attach_file_button_label)
        } else {
            disabledMessageLabel
        }
    val formattedFileSize = Formatter.formatFileSize(LocalContext.current, maxFileSize)
    val limitMessageArgs =
        if (maxFilesAmount > 1) {
            arrayOf<Any>(maxFilesAmount, formattedFileSize)
        } else {
            arrayOf(formattedFileSize)
        }
    val limitMessageLabel =
        pluralStringResource(
            R.plurals.attach_file_limit_message,
            maxFilesAmount,
            *limitMessageArgs
        )
    val foregroundColor =
        if (enabled) {
            LocalAppColorScheme.current.text.brand.primary
        } else {
            LocalAppColorScheme.current.text.disabled.primary
        }

    Box(
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = LocalAppColorScheme.current.border.default.primary,
                    shape = RoundedCornerShape(LocalAppRadius.current.value100)
                ).clickable(
                    enabled = enabled,
                    onClickLabel = stringResource(R.string.attach_file_button_on_click_label),
                    role = Role.Button,
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = indication
                )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(LocalAppSpacing.current.value400)
                    .fillMaxWidth()
        ) {
            Icon(
                imageVector = AppIcons.Upload,
                contentDescription = stringResource(R.string.attach_file_button_icon_alt),
                tint = foregroundColor
            )

            Text(
                text = label,
                style = LocalAppTypography.current.bodyBase,
                color = foregroundColor
            )

            if (enabled) {
                Text(
                    text = limitMessageLabel,
                    style = LocalAppTypography.current.bodySmall,
                    color = LocalAppColorScheme.current.text.default.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun EditPasswordDialog(
    onApplyRequest: (String) -> Unit,
    onDismissRequest: () -> Unit,
    filename: String,
    currentPassword: String,
    modifier: Modifier = Modifier
) {
    var newPassword by rememberSaveable { mutableStateOf(currentPassword) }
    val shape = RoundedCornerShape(LocalAppRadius.current.value100)

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier =
                modifier
                    .background(
                        color = LocalAppColorScheme.current.background.default.primary,
                        shape = shape
                    ).border(
                        width = 1.dp,
                        color = LocalAppColorScheme.current.border.default.primary,
                        shape = shape
                    ).padding(LocalAppSpacing.current.value600)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value600)
            ) {
                EditPasswordDialogHeader()
                EditPasswordDialogFileName(filename)
                EditPasswordDialogInput(
                    password = newPassword,
                    onPasswordChange = { newPassword = it }
                )
                EditPasswordDialogButtons(
                    onApplyRequest = { onApplyRequest(newPassword) },
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}

@Composable
private fun EditPasswordDialogHeader(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.attached_file_password_dialog_title),
            style = LocalAppTypography.current.title5,
            color = LocalAppColorScheme.current.text.brand.primary
        )
        Text(
            text = stringResource(R.string.attached_file_password_dialog_description),
            style = LocalAppTypography.current.bodyBase,
            color = LocalAppColorScheme.current.text.default.primary
        )
    }
}

@Composable
private fun EditPasswordDialogFileName(
    filename: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.attached_file_password_dialog_file_name_label),
            style = LocalAppTypography.current.bodySmall,
            color = LocalAppColorScheme.current.text.default.secondary
        )
        Text(
            text = filename,
            style = LocalAppTypography.current.bodyBase,
            color = LocalAppColorScheme.current.text.default.primary
        )
    }
}

@Composable
private fun EditPasswordDialogInput(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordVisualTransformation = PasswordVisualTransformation()
    val noneVisualTransformation = VisualTransformation.None
    var inputVisualTransformation by remember {
        mutableStateOf<VisualTransformation>(passwordVisualTransformation)
    }
    var showPassword by remember { mutableStateOf(true) }

    val togglePasswordTransformationIcon = if (showPassword) AppIcons.Eye else AppIcons.EyeOff
    val togglePasswordTransformationIconAlt =
        if (showPassword) {
            stringResource(R.string.attached_file_password_dialog_show_button_icon_alt)
        } else {
            stringResource(R.string.attached_file_password_dialog_hide_button_icon_alt)
        }
    TextInput(
        value = password,
        onValueChange = onPasswordChange,
        label = stringResource(R.string.attached_file_password_dialog_input_label),
        trailingButton = {
            AppButton(
                type = ButtonType.Tertiary,
                size = SizeType.Small,
                onClick = {
                    inputVisualTransformation =
                        if (showPassword) {
                            noneVisualTransformation
                        } else {
                            passwordVisualTransformation
                        }
                    showPassword = !showPassword
                },
                displayLabel = false,
                displayIcon = true,
                icon = togglePasswordTransformationIcon,
                iconAlt = togglePasswordTransformationIconAlt
            )
        },
        visualTransformation = inputVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier
    )
}

@Composable
private fun EditPasswordDialogButtons(
    onApplyRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value300,
                alignment = Alignment.End
            ),
        modifier = modifier.fillMaxWidth()
    ) {
        AppButton(
            type = ButtonType.Secondary,
            size = SizeType.Small,
            onClick = onDismissRequest,
            displayLabel = true,
            label = stringResource(R.string.attached_file_password_dialog_cancel_button_label),
            displayIcon = true,
            icon = AppIcons.Cross,
            iconAlt = stringResource(R.string.attached_file_password_dialog_cancel_button_icon_alt)
        )

        AppButton(
            type = ButtonType.Primary,
            size = SizeType.Small,
            onClick = onApplyRequest,
            displayLabel = true,
            label = stringResource(R.string.attached_file_password_dialog_apply_button_label),
            displayIcon = true,
            icon = AppIcons.Check,
            iconAlt = stringResource(R.string.attached_file_password_dialog_apply_button_icon_alt)
        )
    }
}

private fun handleMultipleContentsContract(
    uris: List<Uri>,
    contentResolver: ContentResolver,
    onFileAttach: (AttachedFileData) -> Unit
) {
    uris.forEach {
        val fileData = getFileDataFromUri(it, contentResolver)
        onFileAttach(fileData)
    }
}

private fun handleContentContract(
    uri: Uri?,
    contentResolver: ContentResolver,
    onFileAttach: (AttachedFileData) -> Unit
) {
    if (uri != null) {
        val fileData = getFileDataFromUri(uri, contentResolver)
        onFileAttach(fileData)
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
            arrayOf(
                OpenableColumns.DISPLAY_NAME,
                OpenableColumns.SIZE,
                MediaStore.MediaColumns.TITLE
            ),
            null,
            null,
            null
        )

    val defaultFileName = "unknown_name"
    var displayName = ""
    var fileSize: Long = -1
    cursor?.use {
        if (!it.moveToFirst()) {
            return@use
        }

        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        val titleIndex = it.getColumnIndex(MediaStore.MediaColumns.TITLE)
        displayName =
            it.getStringOrNull(nameIndex) ?: it.getStringOrNull(titleIndex)
                ?: "${defaultFileName}_${Random.nextInt()}"
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
            settings = FileAttachmentSettings(maxFilesAmount = 1, maxFileSize = 1048576),
            pickMultipleFiles = false
        )
    }
}