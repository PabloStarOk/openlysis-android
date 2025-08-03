package com.openlysis.data.analysis.request

import java.io.Closeable
import java.io.InputStream

/**
 * Represents a single file to be analyzed.
 *
 * @property stream The input stream to read the file data from.
 * @property name The name of the file.
 * @property size The size of the file in bytes.
 * @property mimeType The MIME type of the file.
 * @property password An optional password to unlock the file, if required.
 */
data class Attachment(
    val stream: InputStream,
    val name: String,
    val size: Long,
    val mimeType: String,
    val password: String?
) : Closeable {
    private var closed = false

    /**
     * Closes the underlying input stream if it has not already been closed.
     * Ensures that the close operation is idempotent.
     */
    override fun close() {
        if (closed) {
            return
        }

        closed = true
        stream.close()
    }
}