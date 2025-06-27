package com.openlysis.feature.tools.data

import androidx.compose.runtime.Immutable

/**
 * Configuration settings for data analysis operations.
 *
 * @property defaultCountryCode The default country code used for phone number detection in the backend API.
 * @property reanalyzeEmails Flag indicating whether to reanalyze email messages.
 * @property reanalyzeSms Flag indicating whether to reanalyze SMS messages.
 * @property reanalyzeUrls Flag indicating whether to reanalyze URLs.
 * @property reanalyzeFiles Flag indicating whether to reanalyze files.
 */
@Immutable
data class AnalysisSettings(
    val defaultCountryCode: String,
    val reanalyzeEmails: Boolean,
    val reanalyzeSms: Boolean,
    val reanalyzeUrls: Boolean,
    val reanalyzeFiles: Boolean
)