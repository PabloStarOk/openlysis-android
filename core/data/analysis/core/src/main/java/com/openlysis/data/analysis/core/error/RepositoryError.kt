package com.openlysis.data.analysis.core.error

/**
 * Represents different types of API errors that can occur during remote requests.
 */
sealed class RepositoryError {
    /** Indicates a 400 Bad Request error. */
    object BadRequest : RepositoryError()

    /** Indicates a resource was not found (404 status code or local data source error). */
    object NotFound : RepositoryError()

    /** Indicates a 5xx Server error. */
    object Server : RepositoryError()

    /** Indicates an access denied or unauthorized error. */
    object AccessDenied : RepositoryError()

    /** Indicates the service is unavailable. */
    object Unavailable : RepositoryError()

    /** Indicates the server is not reachable (e.g., DNS or timeout error). */
    object ServerUnreachable : RepositoryError()

    /** Indicates a network connectivity error. */
    object Network : RepositoryError()

    /** Indicates that the operation was canceled, typically by the user or system. */
    object OperationCanceled : RepositoryError()

    /** Indicates an unknown or unexpected error. */
    object Unknown : RepositoryError()
}