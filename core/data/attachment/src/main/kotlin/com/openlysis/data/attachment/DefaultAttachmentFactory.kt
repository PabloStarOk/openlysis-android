package com.openlysis.data.attachment

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.request.Attachment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.random.Random

/**
 * Default implementation of [AttachmentFactory] that creates attachments from Android URIs using Android's [ContentResolver].
 *
 * @property context The application context used for accessing resources, file data and metadata.
 */
internal class DefaultAttachmentFactory(
    @ApplicationContext private val context: Context
) : AttachmentFactory {
    override fun create(
        uri: Uri,
        password: String?
    ): Outcome<Attachment> {
        val mimeType =
            context.contentResolver.getType(uri)
                ?: FactoryDefaults.DEFAULT_MIME_TYPE

        val inputStream: InputStream
        try {
            inputStream = context.contentResolver.openInputStream(uri)
                ?: return Outcome.Failure(AttachmentCreationError.NullInputStream(uri))
        } catch (e: FileNotFoundException) {
            return Outcome.Failure(AttachmentCreationError.FileNotFound(uri, e))
        }

        val cursor =
            context.contentResolver.query(
                uri,
                arrayOf(
                    OpenableColumns.DISPLAY_NAME,
                    OpenableColumns.SIZE,
                    MediaStore.MediaColumns.TITLE
                ),
                null,
                null,
                null
            )

        if (cursor == null) {
            return Outcome.Failure(AttachmentCreationError.NullMetadataCursor(uri))
        }

        val unknownFileName = context.getString(R.string.attachment_factory_unknown_file_name)
        val displayName: String
        val fileSize: Long
        cursor.use {
            if (!it.moveToFirst()) {
                return Outcome.Failure(AttachmentCreationError.EmptyMetadataCursor(uri))
            }

            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
            val titleIndex = it.getColumnIndex(MediaStore.MediaColumns.TITLE)
            displayName =
                it.getStringOrNull(nameIndex) ?: it.getStringOrNull(titleIndex)
                    ?: "${unknownFileName}_${Random.nextInt()}"
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