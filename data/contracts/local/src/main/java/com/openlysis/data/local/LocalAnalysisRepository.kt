package com.openlysis.data.local

/**
 * Repository interface for local storage operations on analysis data.
 *
 * @param TAnalysis The type of analysis entity.
 */
interface LocalAnalysisRepository<TAnalysis> where TAnalysis : Any {
    /**
     * Saves the given analysis entity to local storage.
     *
     * @param analysis The analysis entity to save.
     */
    suspend fun save(analysis: TAnalysis)

    /**
     * Updates the given analysis entity in local storage.
     *
     * @param analysis The analysis entity to update.
     */
    suspend fun update(analysis: TAnalysis)

    /**
     * Retrieves a paginated list of analysis entities from local storage.
     *
     * @param page The page number to retrieve.
     * @param size The number of entities per page.
     * @return A list of analysis entities.
     */
    suspend fun getMany(
        page: Int,
        size: Int
    ): List<TAnalysis>
}