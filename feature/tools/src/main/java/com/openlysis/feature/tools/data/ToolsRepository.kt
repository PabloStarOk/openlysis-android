package com.openlysis.feature.tools.data

/**
 * Interface for repositories that provide lists of analysis tools used within the application.
 */
internal sealed interface ToolsRepository {
    fun getMessageAnalysisTools(): List<Tool>

    fun getDataAnalysisTools(): List<Tool>
}