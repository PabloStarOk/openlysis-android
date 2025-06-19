package com.openlysis.data.remote.repository

import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.response.AnalyzeResponse
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.remote.OpenlysisService
import com.openlysis.data.remote.constant.ApiFields
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository implementation for handling file multi-analysis operations.
 *
 * Provides methods to analyze files and retrieve multi-analysis results from the remote API.
 *
 * @param service The [OpenlysisService] used to perform network operations.
 */
internal class FileMultiAnalysisRepository
    @Inject
    constructor(
        service: OpenlysisService
    ) : BaseAnalysisRepository<AnalyzeFile, FileMultiAnalysis>(service) {
        /**
         * Analyzes a file by sending it to the remote service.
         *
         * @param request The [AnalyzeFile] request containing the file and analysis parameters.
         * @return A [Response] containing the [AnalyzeResponse] from the API if successful, or an error response otherwise.
         */
        override suspend fun handleAnalyze(request: AnalyzeFile): Response<AnalyzeResponse> =
            service.analyzeFile(
                file =
                    request.attachment.file.asFormDataPart(
                        name = ApiFields.FILE,
                        mimeType = request.attachment.mimeType
                    ),
                password = request.attachment.password?.asPlainRequestBody(),
                reanalyze = request.reanalyze
            )

        /**
         * Retrieves the multi-analysis result for a given file ID.
         *
         * @param id The unique identifier for the file analysis.
         * @return A [Response] containing the [FileMultiAnalysis] model if successful, or an error response otherwise.
         */
        override suspend fun handleGet(id: String): Response<FileMultiAnalysis> {
            val response = service.getFileMultiAnalysis(id)
            return convertToModelIfSuccess(response) { it.convertToModel() }
        }
    }