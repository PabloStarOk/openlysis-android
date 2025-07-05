package com.openlysis.feature.results.components

import androidx.compose.runtime.Immutable

/**
 * Represents the statistics of different verdict types in the analysis results.
 *
 * @property cleanVerdicts The count of clean (safe) verdicts
 * @property suspiciousVerdicts The count of suspicious verdicts
 * @property maliciousVerdicts The count of malicious verdicts
 * @property unknownVerdicts The count of unknown/unclassified verdicts, defaults to 0
 */
@Immutable
internal data class VerdictStatsState(
    val cleanVerdicts: Int,
    val suspiciousVerdicts: Int,
    val maliciousVerdicts: Int,
    val unknownVerdicts: Int = 0
) {
    companion object {
        /** Represents a state with all verdict counts set to zero */
        val Zero = VerdictStatsState(0, 0, 0, 0)
    }
}