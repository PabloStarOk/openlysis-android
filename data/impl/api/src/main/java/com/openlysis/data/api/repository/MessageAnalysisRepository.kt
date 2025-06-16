package com.openlysis.data.api.repository

import com.openlysis.data.api.OpenlysisService
import com.openlysis.data.api.constant.ApiFields
import com.openlysis.data.remote.request.AnalyzeMessage
import com.openlysis.data.remote.response.AnalyzeResponse
import com.openlysis.models.message.MessageAnalysis

/**
 * Repository implementation for handling message analysis operations.
 *
 * @param service The [OpenlysisService] used to perform network operations.
 */
internal class MessageAnalysisRepository(
    service: OpenlysisService
) : BaseAnalysisRepository<AnalyzeMessage, MessageAnalysis>(service) {
    /**
     * Sends an analysis request for a message, including any attachments.
     *
     * @param request The [AnalyzeMessage] request containing the message data and reanalyze flag.
     * @return The [AnalyzeResponse] from the API.
     */
    override suspend fun handleAnalyze(request: AnalyzeMessage): AnalyzeResponse {
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

        return service.analyzeMessage(
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
     * @return The [MessageAnalysis] model.
     */
    override suspend fun handleGet(id: String): MessageAnalysis =
        service.getMessageAnalysis(id).convertToModel()
}