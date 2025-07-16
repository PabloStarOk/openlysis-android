package com.openlysis.feature.results.components.detail

import androidx.compose.runtime.Immutable
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict

/**
 * Data class representing the result of a service analysis.
 *
 * @property serviceName Name of the service.
 * @property status Current status of the analysis.
 * @property verdict Final verdict of the analysis.
 * @property threatScore Optional threat score assigned by the service.
 */
@Immutable
internal data class ServiceResultData(
    val serviceName: String,
    val status: AnalysisStatus?,
    val verdict: Verdict,
    val threatScore: Int?
)