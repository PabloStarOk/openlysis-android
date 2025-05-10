package com.openlysis.feature.tools.data

/**
 * Interface for repositories that provide lists of tools used within the application.
 * Implementations of this interface are responsible for retrieving different categories of tools.
 */
internal sealed interface ToolsRepository {
    fun getMessageAnalysisTools(): List<Tool>

    fun getDataAnalysisTools(): List<Tool>

    fun getReputationTools(): List<Tool>
}