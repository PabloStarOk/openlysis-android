package com.openlysis.feature.results.components.detail

import androidx.compose.runtime.Immutable
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import java.time.Instant

/**
 * Data class representing the required data by the details screen scaffold.
 *
 * @property heroInfoCardData Optional pair containing hero info card title and value.
 * @property status The current analysis status.
 * @property verdict The verdict of the analysis.
 * @property threatScore Optional threat score associated with the analysis.
 * @property startedDate The date and time when the analysis started.
 * @property informationSectionTitle Optional label for the information section.
 * @property informationSectionItems Optional list of key-value pairs for the information section.
 */
@Immutable
internal data class DetailsScreenScaffoldData(
    val heroInfoCardData: Pair<String, String>?,
    val status: AnalysisStatus,
    val verdict: Verdict,
    val threatScore: Int?,
    val startedDate: Instant,
    val informationSectionTitle: String?,
    val informationSectionItems: List<Pair<String, String>>?
)