package com.openlysis.data.attachment

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.database.getLongOrNull
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.request.Attachment
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * Default implementation of [AttachmentFactory] that creates attachments from Android URIs using Android's [ContentResolver].
 *
 * @property contentResolver The system's content resolver used to access file data and metadata
 */
internal class DefaultAttachmentFactory(
    private val contentResolver: ContentResolver
) : AttachmentFactory {
    override fun create(
        uri: Uri,
        password: String?
    ): Outcome<Attachment> {
        val mimeType =
            contentResolver.getType(uri)
                ?: FactoryDefaults.DEFAULT_MIME_TYPE

        val inputStream: InputStream
        try {
            inputStream = contentResolver.openInputStream(uri)
                ?: return Outcome.Failure(AttachmentCreationError.NullInputStream(uri))
        } catch (e: FileNotFoundException) {
            return Outcome.Failure(AttachmentCreationError.FileNotFound(uri, e))
        }

        val cursor =
            contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                null,
                null,
                null
            )

        if (cursor == null) {
            return Outcome.Failure(AttachmentCreationError.NullMetadataCursor(uri))
        }

        val displayName: String
        val fileSize: Long
        cursor.use {
            if (!it.moveToFirst()) {
                return Outcome.Failure(AttachmentCreationError.EmptyMetadataCursor(uri))
            }

            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
            displayName = it.getString(nameIndex)
            fileSize = it.getLongOrNull(sizeIndex) ?: FactoryDefaults.UNKNOWN_FILE_SIZE
        }

        val attachment =
            Attachment(
                stream = inputStream,
                name = displayName,
                mimeType = mimeType,
                size = fileSize,
                password = password
            )
        return Outcome.Success(attachment)
    }
}