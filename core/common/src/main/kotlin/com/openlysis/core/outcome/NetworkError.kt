package com.openlysis.core.outcome

/**
 * Represents different types of API errors that can occur during network operations.
 */
sealed interface NetworkError : AppError {
    /** Indicates a 400 Bad Request error. */
    data object BadRequest : NetworkError

    /** Indicates a resource was not found (404 status code or local data source error). */
    data object NotFound : NetworkError

    /** Indicates a 5xx Server error. */
    data object Server : NetworkError

    /** Indicates an access denied or unauthorized error. */
    data object AccessDenied : NetworkError

    /** Indicates the service is unavailable. */
    data object Unavailable : NetworkError

    /** Indicates the server is not reachable (e.g., DNS or timeout error). */
    data object ServerUnreachable : NetworkError

    /** Indicates a network connectivity error. */
    data object Network : NetworkError

    /** Indicates that the operation was canceled, typically by the user or system. */
    data object OperationCanceled : NetworkError

    /** Indicates an unknown or unexpected error. */
    data object Unknown : NetworkError
}