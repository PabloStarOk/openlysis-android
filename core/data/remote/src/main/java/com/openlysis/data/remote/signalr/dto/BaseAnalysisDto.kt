package com.openlysis.data.remote.signalr.dto

import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Model

/**
 * Base interface for analysis DTOs sent over SignalR for [Model] types.
 *
 * @param TAnalysis The type of model this DTO converts to.
 * @property id Unique identifier for the analysis.
 * @property startedDateMillis Timestamp in milliseconds when the analysis started.
 * @property status Current status of the analysis.
 */
internal interface BaseAnalysisDto<TAnalysis : Model> {
    /**
     * Unique identifier for the analysis.
     */
    val id: String

    /**
     * Timestamp in milliseconds when the analysis started.
     */
    val startedDateMillis: Long

    /**
     * Current status of the analysis.
     */
    val status: AnalysisStatus

    /**
     * Converts this DTO to its corresponding model.
     *
     * @return The model representation of this DTO.
     */
    fun convertToModel(): TAnalysis
}