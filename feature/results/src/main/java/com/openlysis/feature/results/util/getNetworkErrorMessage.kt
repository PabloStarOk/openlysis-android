package com.openlysis.feature.results.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.openlysis.core.outcome.AppError
import com.openlysis.core.outcome.NetworkError
import com.openlysis.feature.results.R

/**
 * Returns a localized error message string for a given [AppError].
 *
 * Maps specific [NetworkError] types to corresponding string resources.
 *
 * @param error The [AppError] to display a message for.
 * @return The localized error message string.
 */
@Composable
internal fun getNetworkErrorMessage(error: AppError): String {
    val resourceId =
        when (error) {
            is NetworkError.Server -> R.string.error_analysis_repository_server
            is NetworkError.Network -> R.string.error_analysis_repository_network
            is NetworkError.ServerUnreachable ->
                R.string.error_analysis_repository_server_unreachable
            is NetworkError.Unavailable ->
                R.string.error_analysis_repository_unavailable
            else -> R.string.error_analysis_repository_generic
        }
    return stringResource(resourceId)
}