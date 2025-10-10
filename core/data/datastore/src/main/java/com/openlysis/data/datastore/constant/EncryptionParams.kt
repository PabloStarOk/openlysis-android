package com.openlysis.data.datastore.constant

import android.security.keystore.KeyProperties

/**
 * Holds constants to encrypt and decrypt the user authentication data.
 *
 * @property ALGORITHM The encryption algorithm (AES).
 * @property BLOCK_MODE The block mode for encryption (GCM).
 * @property PADDING The padding scheme (None).
 * @property TRANSFORMATION The transformation string for cipher initialization.
 * @property KEY_SIZE The size of the encryption key in bits.
 * @property PROVIDER The provider name.
 */
internal object EncryptionParams {
    const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    const val KEY_SIZE = 256
    const val PROVIDER = "AndroidKeyStore"
    const val KEY_ALIAS = "OpenlysisEncryptionKey"
}