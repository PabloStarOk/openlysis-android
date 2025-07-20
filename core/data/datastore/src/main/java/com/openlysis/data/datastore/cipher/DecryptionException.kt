package com.openlysis.data.datastore.cipher

/**
 * Exception thrown when decryption of data fails.
 *
 * This may occur due to:
 * - Tampered or corrupted encrypted data
 * - Use of an incorrect decryption key
 * - Cryptographic integrity violations
 *
 * @param message Detailed error message describing the failure.
 * @param cause The underlying cause of the decryption failure.
 */
internal class DecryptionException(
    message: String,
    cause: Throwable
) : Exception(message, cause)