package com.openlysis.models.analysis

/**
 * The status of an analysis process.
 */
enum class AnalysisStatus {
    Queued,
    InProgress,
    Completed,
    Failed,
    Timeout;

    companion object {
        /**
         * Parses the given string to an [AnalysisStatus] enum value.
         *
         * @param value The string representation of the status.
         * @param ignoreCase Whether to ignore case when matching the status.
         * @return The corresponding [AnalysisStatus] value.
         * @throws IllegalArgumentException if the value does not match any status.
         */
        fun parse(
            value: String,
            ignoreCase: Boolean = true
        ): AnalysisStatus =
            AnalysisStatus.entries.find {
                it.name.equals(value, ignoreCase)
            } ?: throw IllegalArgumentException("Invalid analysis status: $value")
    }
}