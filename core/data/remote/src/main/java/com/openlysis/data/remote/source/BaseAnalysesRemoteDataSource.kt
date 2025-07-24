package com.openlysis.data.remote.source

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.core.request.Attachment
import com.openlysis.data.analysis.core.response.AnalyzeResponse
import com.openlysis.data.analysis.core.source.AnalysesRemoteDataSource
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.remote.AttachmentRequestBody
import com.openlysis.data.remote.NetworkApiCaller
import com.openlysis.data.remote.OpenlysisApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

/**
 * Abstract base data source for analyses operations.
 *
 * @param TRequest The type of the request object for analysis operations.
 * @param TModel The type of the model returned by analysis operations.
 * @property api The Retrofit service used to perform remote API calls.
 */
internal abstract class BaseAnalysesRemoteDataSource<TRequest, TModel>(
    protected val api: OpenlysisApi
) : NetworkApiCaller(),
    AnalysesRemoteDataSource<TRequest, TModel> where TRequest : Any, TModel : Model {
    override suspend fun analyze(request: TRequest): Outcome<AnalyzeResponse> =
        callApiSafely {
            handleAnalyze(request)
        }

    override suspend fun getById(id: String): Outcome<TModel> =
        callApiSafely {
            handleGet(id)
        }

    override suspend fun getMany(
        page: Int,
        size: Int
    ): Outcome<List<TModel>> =
        callApiSafely {
            handleGetMany(page, size)
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
     * Retrieves a list of model instances for the specified page and size.
     *
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return An [Outcome] containing a list of [TModel] if successful, or an error otherwise.
     */
    protected abstract suspend fun handleGetMany(
        page: Int,
        size: Int
    ): Response<List<TModel>>

    /**
     * Converts an [Attachment] to a [MultipartBody.Part] for use in multipart form data requests.
     *
     * @param fieldName The name of the form field.
     * @receiver The [Attachment] to be converted.
     * @return A [MultipartBody.Part] representing the attachment as form data.
     */
    protected fun Attachment.asFormDataPart(fieldName: String): MultipartBody.Part =
        MultipartBody.Part.createFormData(
            fieldName,
            this.name,
            AttachmentRequestBody(this)
        )

    /**
     * Converts a [String] to a [RequestBody] with MIME type "text/plain".
     *
     * @receiver The string to convert.
     * @return A [RequestBody] containing the string.
     */
    protected fun String.asPlainRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaType())
}