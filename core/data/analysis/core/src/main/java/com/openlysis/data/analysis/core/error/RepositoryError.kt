package com.openlysis.data.analysis.core.error

import com.openlysis.data.analysis.model.common.AnalysisError

/**
 * Represents different types of API errors that can occur during remote requests.
 */
sealed interface RepositoryError : AnalysisError {
    /** Indicates a 400 Bad Request error. */
    data object BadRequest : RepositoryError

    /** Indicates a resource was not found (404 status code or local data source error). */
    data object NotFound : RepositoryError

    /** Indicates a 5xx Server error. */
    data object Server : RepositoryError

    /** Indicates an access denied or unauthorized error. */
    data object AccessDenied : RepositoryError

    /** Indicates the service is unavailable. */
    data object Unavailable : RepositoryError

    /** Indicates the server is not reachable (e.g., DNS or timeout error). */
    data object ServerUnreachable : RepositoryError

    /** Indicates a network connectivity error. */
    data object Network : RepositoryError

    /** Indicates that the operation was canceled, typically by the user or system. */
    data object OperationCanceled : RepositoryError

    /** Indicates an unknown or unexpected error. */
    data object Unknown : RepositoryError
}