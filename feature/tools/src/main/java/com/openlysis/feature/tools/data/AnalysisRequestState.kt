package com.openlysis.feature.tools.data

import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.AnalysisError
import com.openlysis.data.analysis.model.message.MessageAnalysis

/**
 * Represents the state of an analysis request.
 */
internal sealed interface AnalysisRequestState {
    /**
     * Represents a successful analysis request.
     */
    sealed interface Success : AnalysisRequestState {
        /**
         * Represents a successful message analysis request.
         * @property initialValue The initial result of [MessageAnalysis] which must be updated until its completion.
         */
        data class Message(
            val initialValue: MessageAnalysis
        ) : Success

        /**
         * Represents a successful file analysis request.
         * @property initialValue The initial result of [FileMultiAnalysis] which must be updated until its completion.
         */
        data class File(
            val initialValue: FileMultiAnalysis
        ) : Success

        /**
         * Represents a successful URL analysis request.
         * @property initialValue The initial result of [UrlMultiAnalysis] which must be updated until its completion.
         */
        data class Url(
            val initialValue: UrlMultiAnalysis
        ) : Success
    }

    /**
     * Represents a failed analysis request.
     * @property error The error details of the failed analysis.
     */
    data class Failure(
        val error: AnalysisError
    ) : AnalysisRequestState

    /**
     * Represents an ongoing analysis request.
     */
    data object InProgress : AnalysisRequestState

    /**
     * Represents an initial or reset state where no analysis has been requested.
     */
    data object None : AnalysisRequestState
}