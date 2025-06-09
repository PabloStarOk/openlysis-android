package com.openlysis.models.analysis

/**
 * The status of an analysis process.
 */
enum class AnalysisStatus {
    Queued,
    InProgress,
    Completed,
    Failed,
    Timeout
}