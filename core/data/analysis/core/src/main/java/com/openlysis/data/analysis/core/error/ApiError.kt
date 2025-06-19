package com.openlysis.data.analysis.core.error

/**
 * Represents different types of API errors that can occur during remote requests.
 */
sealed class ApiError {
    /** Indicates a 400 Bad Request error. */
    object BadRequest : ApiError()

    /** Indicates a 404 Not Found error. */
    object NotFound : ApiError()

    /** Indicates a 5xx Server error. */
    object Server : ApiError()

    /** Indicates an access denied or unauthorized error. */
    object AccessDenied : ApiError()

    /** Indicates the service is unavailable. */
    object Unavailable : ApiError()

    /** Indicates the server is not reachable (e.g., DNS or timeout error). */
    object ServerUnreachable : ApiError()

    /** Indicates a network connectivity error. */
    object Network : ApiError()

    /** Indicates that the operation was canceled, typically by the user or system. */
    object OperationCanceled : ApiError()

    /** Indicates an unknown or unexpected error. */
    object Unknown : ApiError()
}