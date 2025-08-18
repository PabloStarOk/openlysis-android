package com.openlysis.feature.results.navigation

import androidx.annotation.Keep

/**
 * Represents the type of result used across results (main, previews or details) screens.
 */
@Keep
enum class ResultType {
    Email,
    Sms,
    File,
    Url
}