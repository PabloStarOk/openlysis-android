package com.openlysis.data.remote

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.error.RepositoryError
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Abstract base class for making network API calls.
 * Provides utility methods for safely executing API requests and handling responses.
 */
internal abstract class NetworkApiCaller {
    /**
     * Converts the response body from a DTO to an application's model type if the response is successful.
     *
     * @param response The original Retrofit [Response] containing the DTO type.
     * @param convertToModel A function to convert the DTO type to the target model type.
     * @return A new [Response] containing the converted [TModel] if successful, or an error response preserving the original error details.
     */
    protected fun <TDto, TModel> convertToModelIfSuccess(
        response: Response<TDto>,
        convertToModel: (TDto) -> TModel
    ): Response<TModel> {
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            Response.success(convertToModel(body))
        } else {
            Response.error(
                response.code(),
                response.errorBody() ?: "".toResponseBody()
            )
        }
    }

    /**
     * Executes the given API call safely, catching common network and HTTP exceptions.
     *
     * @param apiCall The suspend function representing the API call to execute.
     * @return An [Outcome] containing the successful result if successful, or a [RepositoryError] otherwise.
     */
    protected suspend fun <TResult> callApiSafely(
        apiCall: suspend () -> Response<TResult>
    ): Outcome<TResult> where TResult : Any =
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                Outcome.Success(response.body() as TResult)
            } else {
                val error =
                    when (response.code()) {
                        400 -> RepositoryError.BadRequest
                        401, 403 -> RepositoryError.AccessDenied
                        404 -> RepositoryError.NotFound
                        503 -> RepositoryError.Unavailable
                        in 500..599 -> RepositoryError.Server
                        else -> RepositoryError.Unknown
                    }
                Outcome.Failure(error)
            }
        } catch (_: ConnectException) {
            Outcome.Failure(RepositoryError.ServerUnreachable)
        } catch (_: UnknownHostException) {
            Outcome.Failure(RepositoryError.ServerUnreachable)
        } catch (_: IOException) {
            Outcome.Failure(RepositoryError.Network)
        } catch (_: CancellationException) {
            Outcome.Failure(RepositoryError.OperationCanceled)
        }
}