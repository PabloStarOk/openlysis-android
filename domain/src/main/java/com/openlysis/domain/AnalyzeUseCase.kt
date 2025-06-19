package com.openlysis.domain

import com.openlysis.data.local.LocalRepository
import com.openlysis.data.remote.AnalysisRepository
import com.openlysis.data.remote.error.Outcome
import javax.inject.Inject

/**
 * Use case for performing an analysis operation using a remote repository and caching the result locally.
 *
 * @param TRequest The type of the request data to be analyzed. Must be a non-null type.
 * @param TAnalysis The type of the analysis result. Must be a non-null type and have an 'id' property.
 * @property remoteRepo The repository responsible for remote analysis operations (e.g., network calls).
 * @property localRepo The repository responsible for local storage and caching of analysis results.
 */
class AnalyzeUseCase<TRequest, TAnalysis>
    @Inject
    constructor(
        private val remoteRepo: AnalysisRepository<TRequest, TAnalysis>,
        private val localRepo: LocalRepository<TAnalysis>
    ) where TRequest : Any, TAnalysis : Any {
    /**
     * Executes the analysis workflow:
     * - Analyzes the provided [request] remotely.
     * - If successful, fetches the analysis result by id and saves it locally.
     * - Returns the outcome of the fetch operation.
     *
     * @param request The data to be analyzed.
     * @return [Outcome] containing the analysis result or error information.
     */
    suspend operator fun invoke(request: TRequest): Outcome<TAnalysis> {
        val analyzeOutcome = remoteRepo.analyze(request)
        val id =
            when (analyzeOutcome) {
                is Outcome.Success -> analyzeOutcome.model.id
                is Outcome.Failure -> return analyzeOutcome
            }

        val getOutcome = remoteRepo.get(id)
        if (getOutcome is Outcome.Success) {
            localRepo.save(getOutcome.model)
        }

        return getOutcome
    }
}