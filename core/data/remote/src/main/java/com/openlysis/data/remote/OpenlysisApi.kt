package com.openlysis.data.remote

import com.openlysis.data.analysis.core.response.AnalyzeResponse
import com.openlysis.data.remote.constant.ApiEndpoints
import com.openlysis.data.remote.constant.ApiFields
import com.openlysis.data.remote.dto.analysis.FileMultiAnalysisDto
import com.openlysis.data.remote.dto.analysis.GetFileMultiAnalysesDto
import com.openlysis.data.remote.dto.analysis.GetUrlMultiAnalysesDto
import com.openlysis.data.remote.dto.analysis.UrlMultiAnalysisDto
import com.openlysis.data.remote.dto.common.AnalysisType
import com.openlysis.data.remote.dto.message.GetMessageAnalysesDto
import com.openlysis.data.remote.dto.message.MessageAnalysisDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for Openlysis API endpoints.
 */
internal interface OpenlysisApi {
    /**
     * Analyzes a URL.
     *
     * @param url The URL to analyze.
     * @param reanalyze Whether to force reanalysis of the URL.
     * @return A [Response] containing an [AnalyzeResponse] if successful, or an error response otherwise.
     */
    @FormUrlEncoded
    @POST(ApiEndpoints.ANALYZE_URL)
    suspend fun analyzeUrl(
        @Field(ApiFields.URL) url: String,
        @Field(ApiFields.REANALYZE) reanalyze: Boolean
    ): Response<AnalyzeResponse>

    /**
     * Analyzes a file.
     *
     * @param file The file to analyze as a [MultipartBody.Part].
     * @param password Optional password for the file, if protected.
     * @param reanalyze Whether to force reanalysis of the file.
     * @return A [Response] containing an [AnalyzeResponse] if successful, or an error response otherwise.
     */
    @Multipart
    @POST(ApiEndpoints.ANALYZE_FILE)
    suspend fun analyzeFile(
        @Part file: MultipartBody.Part,
        @Part(ApiFields.FILE_PASSWORD) password: RequestBody?,
        @Part(ApiFields.REANALYZE) reanalyze: Boolean
    ): Response<AnalyzeResponse>

    /**
     * Analyzes a message with optional attachments.
     *
     * @param messageType The type of the message.
     * @param sender The sender of the message.
     * @param content The content of the message.
     * @param subject Optional subject of the message.
     * @param attachedFiles Optional list of attached files.
     * @param attachedFilesPasswords Optional map of passwords for attached files where the key represents the filename and the value its password.
     * @param countryCode Optional country code to identify detected phone numbers.
     * @param reanalyze Whether to force reanalysis of the message.
     * @return A [Response] containing an [AnalyzeResponse] if successful, or an error response otherwise.
     */
    @Multipart
    @POST(ApiEndpoints.ANALYZE_MESSAGE)
    suspend fun analyzeMessage(
        @Part(ApiFields.MESSAGE_TYPE) messageType: RequestBody,
        @Part(ApiFields.MESSAGE_SENDER) sender: RequestBody,
        @Part(ApiFields.MESSAGE_CONTENT) content: RequestBody,
        @Part(ApiFields.MESSAGE_SUBJECT) subject: RequestBody?,
        @Part attachedFiles: List<MultipartBody.Part>?,
        @Part(ApiFields.MESSAGE_PASSWORDS) attachedFilesPasswords: Map<String, String>?,
        @Part(ApiFields.MESSAGE_COUNTRY_CODE) countryCode: RequestBody?,
        @Part(ApiFields.REANALYZE) reanalyze: Boolean
    ): Response<AnalyzeResponse>

    /**
     * Retrieves multi-analysis results for a URL.
     *
     * @param id The analysis ID.
     * @return A [Response] containing a [UrlMultiAnalysisDto] if successful, or an error response otherwise.
     */
    @GET(ApiEndpoints.GET_URL_MULTI_ANALYSIS)
    suspend fun getUrlMultiAnalysis(
        @Path(ApiFields.ANALYSIS_ID) id: String
    ): Response<UrlMultiAnalysisDto>

    /**
     * Retrieves multi-analysis results for a file.
     *
     * @param id The analysis ID.
     * @return A [Response] containing a [FileMultiAnalysisDto] if successful, or an error response otherwise.
     */
    @GET(ApiEndpoints.GET_FILE_MULTI_ANALYSIS)
    suspend fun getFileMultiAnalysis(
        @Path(ApiFields.ANALYSIS_ID) id: String
    ): Response<FileMultiAnalysisDto>

    /**
     * Retrieves analysis results for a message.
     *
     * @param id The analysis ID.
     * @return A [Response] containing a [MessageAnalysisDto] if successful, or an error response otherwise.
     */
    @GET(ApiEndpoints.GET_MESSAGE_ANALYSIS)
    suspend fun getMessageAnalysis(
        @Path(ApiFields.ANALYSIS_ID) id: String
    ): Response<MessageAnalysisDto>

    /**
     * Retrieves a paginated list of [UrlMultiAnalysisDto] objects.
     *
     * @param page The page number to retrieve.
     * @param pageSize The number of items per page.
     * @param type The type of analysis to retrieve which must be fixed to url.
     */
    @GET(ApiEndpoints.GET_ANALYSES)
    suspend fun getUrlMultiAnalyses(
        @Query(ApiFields.PAGE) page: Int,
        @Query(ApiFields.PAGE_SIZE) pageSize: Int,
        @Query(ApiFields.ANALYSIS_TYPE) type: AnalysisType = AnalysisType.Url
    ): Response<GetUrlMultiAnalysesDto>

    /**
     * Retrieves a paginated list of [FileMultiAnalysisDto] objects.
     *
     * @param page The page number to retrieve.
     * @param pageSize The number of items per page.
     * @param type The type of analysis to retrieve which must be fixed to file.
     */
    @GET(ApiEndpoints.GET_ANALYSES)
    suspend fun getFileMultiAnalyses(
        @Query(ApiFields.PAGE) page: Int,
        @Query(ApiFields.PAGE_SIZE) pageSize: Int,
        @Query(ApiFields.ANALYSIS_TYPE) type: AnalysisType = AnalysisType.File
    ): Response<GetFileMultiAnalysesDto>

    /**
     * Retrieves a paginated list of [MessageAnalysisDto] objects.
     *
     * @param page The page number to retrieve.
     * @param pageSize The number of items per page.
     * @param type The type of analysis to retrieve which must be email or SMS.
     */
    @GET(ApiEndpoints.GET_ANALYSES)
    suspend fun getMessageAnalyses(
        @Query(ApiFields.PAGE) page: Int,
        @Query(ApiFields.PAGE_SIZE) pageSize: Int,
        @Query(ApiFields.ANALYSIS_TYPE) type: AnalysisType
    ): Response<GetMessageAnalysesDto>
}