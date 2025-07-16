package com.openlysis.feature.results.components.preview

import androidx.compose.runtime.Immutable
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn

/**
 * Represents the state of filters for analysis results.
 *
 * @property sortingFields List of fields by which the results can be sorted
 * @property selectedVerdicts List of verdicts currently selected for filtering
 * @property selectedStatuses List of analysis statuses currently selected for filtering
 * @property startDateMillis Start date of the date range filter in milliseconds since epoch
 * @property endDateMillis End date of the date range filter in milliseconds since epoch
 */
@Immutable
internal data class FiltersState(
    val sortingFields: List<SortableField>,
    val selectedVerdicts: List<Verdict>,
    val selectedStatuses: List<AnalysisStatus>,
    val startDateMillis: Long,
    val endDateMillis: Long
) {
    companion object {
        val Default =
            FiltersState(
                sortingFields = listOf(SortableField.Date),
                selectedVerdicts = Verdict.entries,
                selectedStatuses = AnalysisStatus.entries,
                startDateMillis =
                    Clock.System
                        .todayIn(TimeZone.currentSystemDefault())
                        .let { today ->
                            LocalDate(today.year, 1, 1)
                                .atTime(0, 0)
                                .toInstant(TimeZone.currentSystemDefault())
                                .toEpochMilliseconds()
                        },
                endDateMillis = Clock.System.now().toEpochMilliseconds()
            )
    }
}