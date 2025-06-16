package com.openlysis.data.api.repository

import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.api.constant.ApiFields
import com.openlysis.data.remote.request.AnalyzeFile
import com.openlysis.data.remote.response.AnalyzeResponse
import com.openlysis.models.analysis.FileMultiAnalysis

/**
 * Repository implementation for handling file multi-analysis operations.
 *
 * @param service The [OpenlysisService] used to perform network operations.
 */
internal class FileMultiAnalysisRepository(
    service: OpenlysisService
) : BaseAnalysisRepository<AnalyzeFile, FileMultiAnalysis>(service) {
    /**
     * Sends an analysis request for a file.
     *
     * @param request The [AnalyzeFile] request containing the file attachment and reanalyze flag.
     * @return The [AnalyzeResponse] from the API.
     */
    override suspend fun handleAnalyze(request: AnalyzeFile): AnalyzeResponse =
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
     * @return The [FileMultiAnalysis] model.
     */
    override suspend fun handleGet(id: String): FileMultiAnalysis =
        service.getFileMultiAnalysis(id).convertToModel()
}