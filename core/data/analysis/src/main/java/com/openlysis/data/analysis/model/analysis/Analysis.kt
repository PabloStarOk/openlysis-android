package com.openlysis.data.analysis.model.analysis

import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.model.common.Verdict

/**
 * The result of an analysis performed by a specific service.
 *
 * @property id The unique identifier of the analysis.
 * @property serviceName The name of the service that conducted the analysis.
 * @property status The current status of the analysis. See [AnalysisStatus] for possible values.
 * @property verdict The overall conclusion reached by the analysis. See [Verdict] for possible values.
 * @property threatScore A numerical score indicating the perceived level of threat assigned by the service.
 */
class Analysis(
    id: String,
    val serviceName: String,
    val status: AnalysisStatus,
    val verdict: Verdict,
    val threatScore: Int?
) : Model(id)