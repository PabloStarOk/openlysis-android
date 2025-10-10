package com.openlysis.data.remote

import com.openlysis.core.outcome.NetworkError
import com.openlysis.core.outcome.Outcome
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Abstract base class for making network API calls.
 * Provides utility methods for safely executing API requests and handling responses.
 *
 * @property dispatcher The [CoroutineDispatcher] on which network calls will be executed.
 */
internal abstract class NetworkApiCaller(
    private val dispatcher: CoroutineDispatcher
) {
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
     * @return An [Outcome] containing the successful result if successful, or a [NetworkError] otherwise.
     */
    protected open suspend fun <TResult> callApiSafely(
        apiCall: suspend () -> Response<TResult>
    ): Outcome<TResult> where TResult : Any =
        withContext(dispatcher) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    Outcome.Success(response.body() as TResult)
                } else {
                    Outcome.Failure(getErrorFromCode(response.code()))
                }
            } catch (ex: Exception) {
                Outcome.Failure(getErrorFromExceptionOrThrow(ex))
            }
        }

    /**
     * Executes the given API call that does not return a body (i.e., `Unit`), safely catching network and HTTP exceptions.
     *
     * @param apiCall The suspend function representing the API call to execute.
     * @return An [Outcome] containing `Unit` if successful, or a [NetworkError] otherwise.
     */
    protected open suspend fun callApiSafelyWithoutResponse(
        apiCall: suspend () -> Response<Unit>
    ): Outcome<Unit> =
        withContext(dispatcher) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    Outcome.Success(Unit)
                } else {
                    Outcome.Failure(getErrorFromCode(response.code()))
                }
            } catch (ex: Exception) {
                Outcome.Failure(getErrorFromExceptionOrThrow(ex))
            }
        }

    private fun getErrorFromCode(code: Int): NetworkError =
        when (code) {
            400 -> NetworkError.BadRequest
            401, 403 -> NetworkError.AccessDenied
            404 -> NetworkError.NotFound
            503 -> NetworkError.Unavailable
            in 500..599 -> NetworkError.Server
            else -> NetworkError.Unknown
        }

    private fun getErrorFromExceptionOrThrow(ex: Exception): NetworkError =
        when (ex) {
            is ConnectException -> NetworkError.ServerUnreachable
            is UnknownHostException -> NetworkError.ServerUnreachable
            is IOException -> NetworkError.Network
            is CancellationException -> NetworkError.OperationCanceled
            else -> throw ex
        }
}