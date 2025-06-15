package com.openlysis.data.api

import com.openlysis.data.api.constant.ApiFields
import com.openlysis.data.remote.AnalysisRepository
import com.openlysis.data.remote.request.AnalyzeFile
import com.openlysis.data.remote.request.AnalyzeMessage
import com.openlysis.data.remote.request.AnalyzeUrl
import com.openlysis.data.remote.response.AnalyzeResponse
import com.openlysis.models.analysis.FileMultiAnalysis
import com.openlysis.models.analysis.UrlMultiAnalysis
import com.openlysis.models.message.MessageAnalysis
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

/**
 * Implementation of [AnalysisRepository] that communicates with the Openlysis API via [OpenlysisService].
 *
 * @property service The Retrofit service used to make API calls.
 */
internal class OpenlysisRepository(
    private val service: OpenlysisService
) : AnalysisRepository {
    override suspend fun analyzeUrl(request: AnalyzeUrl): Result<AnalyzeResponse> {
        try {
            val response =
                service.analyzeUrl(
                    url = request.url.toString(),
                    reanalyze = request.reanalyze
                )
            return Result.success(response)
        } catch (e: HttpException) {
            return Result.failure(e)
        }
    }

    override suspend fun analyzeFile(request: AnalyzeFile): Result<AnalyzeResponse> {
        val attachment = request.attachment
        try {
            val response =
                service.analyzeFile(
                    file =
                        attachment.file.asFormDataPart(
                            name = ApiFields.FILE,
                            mimeType = request.attachment.mimeType
                        ),
                    password = attachment.password?.asPlainRequestBody(),
                    reanalyze = request.reanalyze
                )
            return Result.success(response)
        } catch (e: HttpException) {
            return Result.failure(e)
        }
    }

    override suspend fun analyzeMessage(request: AnalyzeMessage): Result<AnalyzeResponse> {
        val validAttachments =
            request.message.attachments?.filter { a ->
                a.file.isFile && a.file.length() > 0
            }

        val files =
            validAttachments
                ?.filter { a ->
                    a.file.isFile && a.file.length() > 0
                }?.map { a ->
                    a.file.asFormDataPart(
                        name = ApiFields.MESSAGE_FILES,
                        mimeType = a.mimeType
                    )
                }
        val passwords =
            validAttachments
                ?.filter { a -> a.password?.isNotEmpty() == true }
                ?.associate { a ->
                    Pair(a.file.name, a.password as String)
                }

        try {
            val response =
                service.analyzeMessage(
                    messageType =
                        request.message.type.name
                            .asPlainRequestBody(),
                    sender = request.message.sender.asPlainRequestBody(),
                    content = request.message.content.asPlainRequestBody(),
                    subject = request.message.subject?.asPlainRequestBody(),
                    attachedFiles = files,
                    attachedFilesPasswords = passwords,
                    countryCode = request.countryCode.asPlainRequestBody(),
                    reanalyze = request.reanalyze
                )
            return Result.success(response)
        } catch (e: HttpException) {
            return Result.failure(e)
        }
    }

    override suspend fun getUrlMultiAnalysis(id: String): Result<UrlMultiAnalysis> {
        try {
            val response = service.getUrlMultiAnalysis(id)
            return Result.success(response.convertToModel())
        } catch (e: HttpException) {
            return Result.failure(e)
        }
    }

    override suspend fun getFileMultiAnalysis(id: String): Result<FileMultiAnalysis> {
        try {
            val response = service.getFileMultiAnalysis(id)
            return Result.success(response.convertToModel())
        } catch (e: HttpException) {
            return Result.failure(e)
        }
    }

    override suspend fun getMessageAnalysis(id: String): Result<MessageAnalysis> {
        try {
            val response = service.getMessageAnalysis(id)
            return Result.success(response.convertToModel())
        } catch (e: HttpException) {
            return Result.failure(e)
        }
    }

    /**
     * Creates a [MultipartBody.Part] from a [File] for use in multipart HTTP requests.
     *
     * @param name The form field name for the file part.
     * @param mimeType The MIME type of the file.
     * @return A [MultipartBody.Part] representing the file.
     */
    private fun File.asFormDataPart(
        name: String,
        mimeType: String
    ): MultipartBody.Part =
        MultipartBody.Part.createFormData(
            name,
            this.name,
            this.asRequestBody(mimeType.toMediaType())
        )

    /**
     * Converts a [String] to a [RequestBody] with MIME type "text/plain".
     *
     * @receiver The string to convert.
     * @return A [RequestBody] containing the string.
     */
    private fun String.asPlainRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaType())
}