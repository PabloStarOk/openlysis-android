package com.openlysis.data.api.repository

import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.remote.AnalysisRepository
import com.openlysis.data.remote.response.AnalyzeResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

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
    override suspend fun analyze(request: TRequest): Result<AnalyzeResponse> =
        callApiSafely {
            handleAnalyze(request)
        }

    override suspend fun get(id: String): Result<TModel> =
        callApiSafely {
            handleGet(id)
        }

    /**
     * Handles the analysis request for the given input.
     *
     * @param request The request data to analyze.
     * @return The response containing the analysis result.
     */
    internal abstract suspend fun handleAnalyze(request: TRequest): AnalyzeResponse

    /**
     * Retrieves a model instance by its unique identifier.
     *
     * @param id The unique identifier of the model to retrieve.
     * @return The model instance corresponding to the given id.
     */
    internal abstract suspend fun handleGet(id: String): TModel

    /**
     * Executes the given API call safely, catching common network and HTTP exceptions.
     *
     * @param apiCall The suspend function representing the API call to execute.
     * @return A [Result] containing the successful result or the caught exception.
     */
    private suspend fun <TResult> callApiSafely(
        apiCall: suspend () -> TResult
    ): Result<TResult> where TResult : Any =
        try {
            Result.success(apiCall())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: ConnectException) {
            Result.failure(e)
        } catch (e: UnknownHostException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }

    /**
     * Creates a [okhttp3.MultipartBody.Part] from a [java.io.File] for use in multipart HTTP requests.
     *
     * @param name The form field name for the file part.
     * @param mimeType The MIME type of the file.
     * @return A [okhttp3.MultipartBody.Part] representing the file.
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
}