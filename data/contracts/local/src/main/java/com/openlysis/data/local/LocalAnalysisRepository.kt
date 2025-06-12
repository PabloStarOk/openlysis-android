package com.openlysis.data.local

import com.openlysis.models.analysis.FileMultiAnalysis
import com.openlysis.models.analysis.UrlMultiAnalysis
import com.openlysis.models.message.MessageAnalysis

/**
 * Repository interface for managing local analysis data.
 */
interface LocalAnalysisRepository {
    /**
     * Saves a [UrlMultiAnalysis] object to the local data source.
     *
     * @param analysis The analysis to save.
     */
    suspend fun saveUrlMultiAnalysis(analysis: UrlMultiAnalysis)

    /**
     * Saves a [FileMultiAnalysis] object to the local data source.
     *
     * @param analysis The analysis to save.
     */
    suspend fun saveFileMultiAnalysis(analysis: FileMultiAnalysis)

    /**
     * Saves a [MessageAnalysis] object to the local data source.
     *
     * @param analysis The analysis to save.
     */
    suspend fun saveMessageAnalysis(analysis: MessageAnalysis)

    /**
     * Retrieves a paginated list of [UrlMultiAnalysis] objects.
     *
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return A list of [UrlMultiAnalysis] objects.
     */
    suspend fun getUrlMultiAnalyses(
        page: Int,
        size: Int
    ): List<UrlMultiAnalysis>

    /**
     * Retrieves a paginated list of [FileMultiAnalysis] objects.
     *
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return A list of [FileMultiAnalysis] objects.
     */
    suspend fun getFileMultiAnalyses(
        page: Int,
        size: Int
    ): List<FileMultiAnalysis>

    /**
     * Retrieves a paginated list of [MessageAnalysis] objects.
     *
     * @param page The page number to retrieve.
     * @param size The number of items per page.
     * @return A list of [MessageAnalysis] objects.
     */
    suspend fun getMessageAnalyses(
        page: Int,
        size: Int
    ): List<MessageAnalysis>
}