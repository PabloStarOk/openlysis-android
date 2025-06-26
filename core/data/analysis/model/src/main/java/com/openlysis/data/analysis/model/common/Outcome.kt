package com.openlysis.data.analysis.model.common

/**
 * Represents the result of an operation that can either be successful with a value or failed with an error.
 *
 * @param TValue The type of value in case of success. Must be non-null.
 */
sealed interface Outcome<out TValue : Any> {
    /**
     * A success operation.
     *
     * @param value The result of the operation.
     */
    data class Success<TValue>(
        val value: TValue
    ) : Outcome<TValue> where TValue : Any

    /**
     * A failed operation.
     *
     * @param error The error representing the failure of the operation.
     */
    data class Failure(
        val error: AnalysisError
    ) : Outcome<Nothing>
}