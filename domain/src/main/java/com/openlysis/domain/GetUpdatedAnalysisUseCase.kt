package com.openlysis.domain

import com.openlysis.data.local.LocalRepository
import com.openlysis.data.remote.AnalysisRepository
import com.openlysis.data.remote.error.Outcome
import javax.inject.Inject
import kotlin.Any

/**
 * Use case for synchronizing an updated analysis from the remote repository to the local cache.
 *
 * @param TAnalysis The type of the analysis result. Must be a non-null type and have an 'id' property.
 * @property remoteRepo The repository responsible for fetching analysis results from a remote source.
 * @property localRepo The repository responsible for updating analysis results in local storage.
 */
class GetUpdatedAnalysisUseCase<TAnalysis>
    @Inject
    constructor(
        private val remoteRepo: AnalysisRepository<*, TAnalysis>,
        private val localRepo: LocalRepository<TAnalysis>
    ) where TAnalysis : Any {
    /**
     * Fetches the analysis result by [id] from the remote repository and updates the local cache if successful.
     *
     * @param id The identifier of the analysis to fetch.
     * @return [Outcome] containing the analysis result or error information.
     */
    suspend operator fun invoke(id: String): Outcome<TAnalysis> {
        val getOutcome = remoteRepo.get(id)
        if (getOutcome is Outcome.Success) {
            localRepo.update(getOutcome.model)
        }

        return getOutcome
    }
}