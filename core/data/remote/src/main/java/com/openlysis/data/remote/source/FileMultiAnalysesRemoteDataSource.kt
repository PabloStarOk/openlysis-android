package com.openlysis.data.remote.source

import com.openlysis.core.network.AppDispatcher
import com.openlysis.core.network.di.Dispatcher
import com.openlysis.data.analysis.core.request.AnalyzeFile
import com.openlysis.data.analysis.core.response.AnalyzeResponse
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.constant.ApiFields
import com.openlysis.data.remote.dto.common.AnalysisType
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source implementation for handling file multi-analysis operations.
 *
 * Provides methods to analyze files and retrieve multi-analysis results from the remote API.
 *
 * @param api The [OpenlysisApi] used to perform network operations.
 * @param ioDispatcher The coroutine dispatcher used for network operations.
 */
internal class FileMultiAnalysesRemoteDataSource
    @Inject
    constructor(
        api: OpenlysisApi,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ) : BaseAnalysesRemoteDataSource<AnalyzeFile, FileMultiAnalysis>(ioDispatcher, api) {
        /**
         * Analyzes a file by sending it to the API.
         *
         * @param request The [AnalyzeFile] request containing the file and analysis parameters.
         * @return A [Response] containing the [AnalyzeResponse] from the API if successful, or an error response otherwise.
         */
        override suspend fun handleAnalyze(request: AnalyzeFile): Response<AnalyzeResponse> =
            api.analyzeFile(
                file = request.attachment.asFormDataPart(fieldName = ApiFields.FILE),
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
            val response = api.getFileMultiAnalysis(id)
            return convertToModelIfSuccess(response) { it.convertToModel() }
        }

        /**
         * Retrieves a paginated list of file multi-analysis results.
         *
         * @param page The page number to retrieve.
         * @param size The number of items per page.
         * @return A [Response] containing a list of [FileMultiAnalysis] models if successful, or an error response otherwise.
         */
        override suspend fun handleGetMany(
            page: Int,
            size: Int
        ): Response<List<FileMultiAnalysis>> {
            val response =
                api.getFileMultiAnalyses(
                    type = AnalysisType.File,
                    page = page,
                    pageSize = size
                )
            return convertToModelIfSuccess(response) {
                it.analyses.map { a ->
                    a.convertToModel()
                }
            }
        }
    }