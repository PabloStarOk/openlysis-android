package com.openlysis.data.api

import com.openlysis.data.api.constant.ApiEndpoints
import com.openlysis.data.api.constant.ApiFields
import com.openlysis.data.api.dto.analysis.FileMultiAnalysisDto
import com.openlysis.data.api.dto.analysis.UrlMultiAnalysisDto
import com.openlysis.data.api.dto.message.MessageAnalysisDto
import com.openlysis.data.remote.response.AnalyzeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Retrofit interface for Openlysis API endpoints.
 */
internal interface OpenlysisService {
    /**
     * Analyzes a URL.
     *
     * @param url The URL to analyze.
     * @param reanalyze Whether to force reanalysis of the URL.
     * @return [AnalyzeResponse] containing the analysis result.
     */
    @FormUrlEncoded
    @POST(ApiEndpoints.ANALYZE_URL)
    suspend fun analyzeUrl(
        @Field(ApiFields.URL) url: String,
        @Field(ApiFields.REANALYZE) reanalyze: Boolean
    ): AnalyzeResponse

    /**
     * Analyzes a file.
     *
     * @param file The file to analyze as a [MultipartBody.Part].
     * @param password Optional password for the file, if protected.
     * @param reanalyze Whether to force reanalysis of the file.
     * @return [AnalyzeResponse] containing the analysis result.
     */
    @Multipart
    @POST(ApiEndpoints.ANALYZE_FILE)
    suspend fun analyzeFile(
        @Part file: MultipartBody.Part,
        @Part(ApiFields.FILE_PASSWORD) password: RequestBody?,
        @Part(ApiFields.REANALYZE) reanalyze: Boolean
    ): AnalyzeResponse

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
     * @return [AnalyzeResponse] containing the analysis result.
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
    ): AnalyzeResponse

    /**
     * Retrieves multi-analysis results for a URL.
     *
     * @param id The analysis ID.
     * @return [UrlMultiAnalysisDto] containing the multi-analysis result.
     */
    @GET(ApiEndpoints.GET_URL_MULTI_ANALYSIS)
    suspend fun getUrlMultiAnalysis(
        @Path(ApiFields.ANALYSIS_ID) id: String
    ): UrlMultiAnalysisDto

    /**
     * Retrieves multi-analysis results for a file.
     *
     * @param id The analysis ID.
     * @return [FileMultiAnalysisDto] containing the multi-analysis result.
     */
    @GET(ApiEndpoints.GET_FILE_MULTI_ANALYSIS)
    suspend fun getFileMultiAnalysis(
        @Path(ApiFields.ANALYSIS_ID) id: String
    ): FileMultiAnalysisDto

    /**
     * Retrieves analysis results for a message.
     *
     * @param id The analysis ID.
     * @return [MessageAnalysisDto] containing the analysis result.
     */
    @GET(ApiEndpoints.GET_MESSAGE_ANALYSIS)
    suspend fun getMessageAnalysis(
        @Path(ApiFields.ANALYSIS_ID) id: String
    ): MessageAnalysisDto
}