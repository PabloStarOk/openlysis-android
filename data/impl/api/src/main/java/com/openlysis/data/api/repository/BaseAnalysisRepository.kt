package com.openlysis.data.api.repository

import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.remote.AnalysisRepository
import com.openlysis.data.remote.error.ApiError
import com.openlysis.data.remote.error.Outcome
import com.openlysis.data.remote.response.AnalyzeResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Base repository for analysis operations, providing common API call handling and utility methods.
 *
 * @param TRequest The type of the request object for analysis.
 * @param TModel The type of the model returned by the repository.
 * @property service The OpenlysisService used to perform network operations.
 */
internal abstract class BaseAnalysisRepository<TRequest, TModel>(
    protected val service: OpenlysisService
) : AnalysisRepository<TRequest, TModel> where TRequest : Any, TModel : Any {
    override suspend fun analyze(request: TRequest): Outcome<AnalyzeResponse> =
        callApiSafely {
            handleAnalyze(request)
        }

    override suspend fun get(id: String): Outcome<TModel> =
        callApiSafely {
            handleGet(id)
        }

    /**
     * Handles the analysis request for the given input.
     *
     * @param request The request data to analyze.
     * @return A [Response] containing an [AnalyzeResponse] if successful, or an error response otherwise.
     */
    protected abstract suspend fun handleAnalyze(request: TRequest): Response<AnalyzeResponse>

    /**
     * Retrieves a model instance by its unique identifier.
     *
     * @param id The unique identifier of the model to retrieve.
     * @return A [Response] containing a [TModel] if successful, or an error response otherwise.
     */
    protected abstract suspend fun handleGet(id: String): Response<TModel>

    /**
     * Creates a [MultipartBody.Part] from a [File] for use in multipart HTTP requests.
     *
     * @param name The form field name for the file part.
     * @param mimeType The MIME type of the file.
     * @return A [MultipartBody.Part] representing the file.
     */
    protected fun File.asFormDataPart(
        name: String,
        mimeType: String
    ): MultipartBody.Part =
        MultipartBody.Part.createFormData(
            name,
            this.name,
            this.asRequestBody(mimeType.toMediaType())
        )

    /**
     * Converts a [String] to a [okhttp3.RequestBody] with MIME type "text/plain".
     *
     * @receiver The string to convert.
     * @return A [okhttp3.RequestBody] containing the string.
     */
    protected fun String.asPlainRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaType())

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
     * @return An [Outcome] containing the successful result if successful, or an [ApiError] otherwise.
     */
    private suspend fun <TResult> callApiSafely(
        apiCall: suspend () -> Response<TResult>
    ): Outcome<TResult> where TResult : Any =
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                Outcome.Success(response.body() as TResult)
            } else {
                val error =
                    when (response.code()) {
                        400 -> ApiError.BadRequest
                        401, 403 -> ApiError.AccessDenied
                        404 -> ApiError.NotFound
                        503 -> ApiError.Unavailable
                        in 500..599 -> ApiError.Server
                        else -> ApiError.Unknown
                    }
                Outcome.Failure(error)
            }
        } catch (_: ConnectException) {
            Outcome.Failure(ApiError.ServerUnreachable)
        } catch (_: UnknownHostException) {
            Outcome.Failure(ApiError.ServerUnreachable)
        } catch (_: IOException) {
            Outcome.Failure(ApiError.Network)
        } catch (_: CancellationException) {
            Outcome.Failure(ApiError.OperationCanceled)
        }
}