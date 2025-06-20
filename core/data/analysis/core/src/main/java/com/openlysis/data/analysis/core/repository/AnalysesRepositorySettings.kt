package com.openlysis.data.analysis.core.repository

/**
 * Settings to configure implementations of [AnalysesRepository].
 *
 * @property paginationSize The number of items to load per page.
 */
data class AnalysesRepositorySettings(
    val paginationSize: Int
)