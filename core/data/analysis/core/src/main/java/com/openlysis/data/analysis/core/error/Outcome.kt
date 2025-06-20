package com.openlysis.data.analysis.core.error

/**
 * Represents the result of a remote operation, which can be either a success or a failure.
 *
 * @param TModel The type of the successful result model.
 */
sealed class Outcome<out TModel> where TModel : Any {
    /**
     * Indicates a successful outcome.
     *
     * @param model The result model of the operation.
     */
    data class Success<TModel>(
        val model: TModel
    ) : Outcome<TModel>() where TModel : Any

    /**
     * Indicates a failed outcome.
     *
     * @param error The error describing the failure.
     */
    data class Failure(
        val error: RepositoryError
    ) : Outcome<Nothing>()
}