package com.openlysis.data.remote.source

import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.request.AnalyzeMessage
import com.openlysis.data.analysis.response.AnalyzeResponse
import com.openlysis.data.auth.AuthTokensManager
import com.openlysis.data.remote.OpenlysisApi
import com.openlysis.data.remote.constant.ApiFields
import com.openlysis.data.remote.dto.common.AnalysisType
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source implementation for handling message analysis operations.
 *
 * Provides methods to analyze messages (with optional attachments) and retrieve analysis results from the remote API.
 *
 * @param analysisType Type of analysis of the data source, must be email or SMS.
 * @param authTokensManager The manager for authentication tokens.
 * @param api The [OpenlysisApi] used to perform network operations.
 * @param dispatcher The coroutine dispatcher used for network operations.
 */
internal class MessageAnalysesRemoteDataSource
    @Inject
    constructor(
        private val analysisType: AnalysisType,
        authTokensManager: AuthTokensManager,
        api: OpenlysisApi,
        dispatcher: CoroutineDispatcher
    ) : BaseAnalysesRemoteDataSource<AnalyzeMessage, MessageAnalysis>(
            authTokensManager,
            dispatcher,
            api
        ) {
        /**
         * Analyzes a message by preparing its attachments and sending the analysis request.
         *
         * This method filters out empty attachments (size <= 0), prepares valid attachments as multipart
         * form data, and creates a password mapping for password-protected files. The prepared data is
         * then sent to the remote API for analysis.
         *
         * @param request The [AnalyzeMessage] containing the message and its attachments to analyze.
         * @return A [Response] containing the [AnalyzeResponse] from the API if successful, or an error response otherwise.
         * @throws Exception if network request fails or API returns an error.
         */
        override suspend fun handleAnalyze(request: AnalyzeMessage): Response<AnalyzeResponse> {
            val validAttachments =
                request.message.attachments?.filter { a -> a.size > 0 }

            val files =
                validAttachments?.map {
                    it.asFormDataPart(fieldName = ApiFields.MESSAGE_FILES)
                }

            val passwords =
                validAttachments
                    ?.filter { it.password?.isNotEmpty() == true }
                    ?.associate { Pair(it.name, it.password as String) }

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
                    type = analysisType,
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