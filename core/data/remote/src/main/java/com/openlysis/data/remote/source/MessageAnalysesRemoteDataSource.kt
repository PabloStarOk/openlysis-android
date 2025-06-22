package com.openlysis.data.remote.source

import com.openlysis.data.analysis.core.request.AnalyzeMessage
import com.openlysis.data.analysis.core.response.AnalyzeResponse
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.constant.ApiFields
import com.openlysis.data.remote.dto.common.AnalysisType
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source implementation for handling message analysis operations.
 *
 * Provides methods to analyze messages (with optional attachments) and retrieve analysis results from the remote API.
 *
 * @param api The [OpenlysisApi] used to perform network operations.
 */
internal class MessageAnalysesRemoteDataSource
    @Inject
    constructor(
        api: OpenlysisApi
    ) : BaseAnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis>(api) {
        /**
         * Analyzes a message by preparing its attachments and sending the analysis request.
         *
         * Filters valid attachments, prepares files as multipart form data, and associates passwords
         * with their corresponding files if provided. Then, calls the service to analyze the message.
         *
         * @param request The [AnalyzeMessage] containing the message and its attachments to analyze.
         * @return A [Response] containing the [AnalyzeResponse] from the API if successful, or an error response otherwise.
         */
        override suspend fun handleAnalyze(request: AnalyzeMessage): Response<AnalyzeResponse> {
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

            return api.analyzeMessage(
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
        }

        /**
         * Retrieves the analysis result for a given message ID.
         *
         * @param id The unique identifier for the message analysis.
         * @return A [Response] containing the [MessageAnalysis] model if successful, or an error response otherwise.
         */
        override suspend fun handleGet(id: String): Response<MessageAnalysis> {
            val response = api.getMessageAnalysis(id)
            return convertToModelIfSuccess(response) { it.convertToModel() }
        }

        /**
         * Retrieves a paginated list of message analysis results.
         *
         * @param page The page number to retrieve.
         * @param size The number of items per page.
         * @return A [Response] containing a list of [MessageAnalysis] models if successful, or an error response otherwise.
         */
        override suspend fun handleGetMany(
            page: Int,
            size: Int
        ): Response<List<MessageAnalysis>> {
            val response =
                api.getMessageAnalyses(
                    type = AnalysisType.Message,
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