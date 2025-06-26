package com.openlysis.data.attachment

import android.net.Uri
import com.openlysis.data.analysis.core.request.Attachment
import com.openlysis.data.analysis.model.common.Outcome

/**
 * Defines a factory for creating attachments from Android URI sources.
 */
interface AttachmentFactory {
    /**
     * Creates a new attachment from the given Android URI.
     *
     * @param uri An [Uri] pointing to the attachment source.
     * @param password Optional password if the attachment is protected.
     * @return An [Outcome] containing the created [Attachment] if successful.
     */
    fun create(
        uri: Uri,
        password: String?
    ): Outcome<Attachment>
}