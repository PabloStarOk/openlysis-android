package com.openlysis.data.remote

import com.openlysis.data.analysis.request.Attachment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source

/**
 * A custom [RequestBody] implementation for sending an [Attachment] via OkHttp.
 *
 * @property attachment The attachment to be sent in the request body.
 */
class AttachmentRequestBody(
    private val attachment: Attachment
) : RequestBody() {
    /**
     * Returns the MIME type of the attachment as a [MediaType].
     */
    override fun contentType(): MediaType? = attachment.mimeType.toMediaTypeOrNull()

    /**
     * Returns the size of the attachment in bytes.
     */
    override fun contentLength(): Long = attachment.size

    /**
     * Writes the attachment's data to the provided [BufferedSink].
     *
     * Note: The attachment's InputStream lifecycle is managed by the caller.
     * This method does not close the stream.
     *
     * @param sink The sink to write the attachment data to.
     */
    override fun writeTo(sink: BufferedSink) {
        val bufferedSource = attachment.stream.source().buffer()
        sink.writeAll(bufferedSource)
    }
}