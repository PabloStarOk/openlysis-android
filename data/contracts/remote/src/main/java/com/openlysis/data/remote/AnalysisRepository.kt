package com.openlysis.data.remote

import com.openlysis.data.remote.request.AnalyzeFile
import com.openlysis.data.remote.request.AnalyzeMessage
import com.openlysis.data.remote.request.AnalyzeUrl
import com.openlysis.data.remote.response.AnalyzeResponse
import com.openlysis.models.analysis.FileMultiAnalysis
import com.openlysis.models.analysis.UrlMultiAnalysis
import com.openlysis.models.message.MessageAnalysis
import kotlin.Result

/**
 * Defines a repository to retrieve and perform analyses.
 */
interface AnalysisRepository {
    /**
     * Analyzes a URL.
     *
     * @param request The [AnalyzeUrl] object containing the URL to be analyzed.
     * @return A [Result] object containing either an [AnalyzeResponse] on success or an [Exception] on failure.
     */
    suspend fun analyzeUrl(request: AnalyzeUrl): Result<AnalyzeResponse>

    /**
     * Analyzes a file.
     *
     * @param request The [AnalyzeFile] object containing the file to be analyzed.
     * @return A [Result] object containing either an [AnalyzeResponse] on success or an [Exception] on failure.
     */
    suspend fun analyzeFile(request: AnalyzeFile): Result<AnalyzeResponse>

    /**
     * Analyzes an SMS or Email message.
     *
     * @param request The [AnalyzeMessage] object containing the message to be analyzed.
     * @return A [Result] object containing either an [AnalyzeResponse] on success or an [Exception] on failure.
     */
    suspend fun analyzeMessage(request: AnalyzeMessage): Result<AnalyzeResponse>

    /**
     * Retrieves a multi-analysis result for a given URL by its ID.
     *
     * @param id The unique identifier of the URL multi-analysis.
     * @return A [Result] containing the [UrlMultiAnalysis] on success or an [Exception] on failure.
     */
    suspend fun getUrlMultiAnalysis(id: String): Result<UrlMultiAnalysis>

    /**
     * Retrieves a multi-analysis result for a given file by its ID.
     *
     * @param id The unique identifier of the file multi-analysis.
     * @return A [Result] containing the [FileMultiAnalysis] on success or an [Exception] on failure.
     */
    suspend fun getFileMultiAnalysis(id: String): Result<FileMultiAnalysis>

    /**
     * Retrieves a message analysis result by its ID.
     *
     * @param id The unique identifier of the message analysis.
     * @return A [Result] containing the [MessageAnalysis] on success or an [Exception] on failure.
     */
    suspend fun getMessageAnalysis(id: String): Result<MessageAnalysis>
}