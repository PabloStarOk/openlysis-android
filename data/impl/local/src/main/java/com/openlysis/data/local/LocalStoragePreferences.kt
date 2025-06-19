package com.openlysis.data.local

/**
 * Data class representing the preferences for local storage limits.
 *
 * @property maxStoredUrlMultiAnalyses Maximum number of stored URL multi-analyses.
 * @property maxStoredFileMultiAnalyses Maximum number of stored file multi-analyses.
 * @property maxStoredMessageAnalyses Maximum number of stored message analyses.
 * @property maxStoredEmailMultiReputations Maximum number of stored email multi-reputations.
 * @property maxStoredPhoneMultiReputations Maximum number of stored phone multi-reputations.
 */
data class LocalStoragePreferences(
    val maxStoredUrlMultiAnalyses: Int,
    val maxStoredFileMultiAnalyses: Int,
    val maxStoredMessageAnalyses: Int,
    val maxStoredEmailMultiReputations: Int,
    val maxStoredPhoneMultiReputations: Int
)