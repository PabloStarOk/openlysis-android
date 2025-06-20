package com.openlysis.data.analysis.core.repository

import com.openlysis.data.analysis.core.error.Outcome
import com.openlysis.data.analysis.core.source.AnalysesLocalDataSource
import com.openlysis.data.analysis.core.source.AnalysesRemoteDataSource
import javax.inject.Inject

/**
 * Default implementation of [AnalysesRepository] that coordinates between local and remote data sources.
 *
 * @param TRequest The type of the request used for analysis.
 * @param TModel The type of the model being analyzed and stored.
 * @property localDs The local data source for storing and retrieving analysis models.
 * @property remoteDs The remote data source for performing analysis and fetching updated models.
 */
internal class DefaultAnalysesRepository<TRequest, TModel>
    @Inject
    constructor(
        private val settings: AnalysesRepositorySettings,
        private val localDs: AnalysesLocalDataSource<TModel>,
        private val remoteDs: AnalysesRemoteDataSource<TRequest, TModel>
    ) : AnalysesRepository<TRequest, TModel>
    where TRequest : Any, TModel : Any {
    override suspend fun analyze(request: TRequest): Outcome<TModel> {
        val outcome = remoteDs.analyze(request)
        var id = ""
        if (outcome is Outcome.Success) {
            id = outcome.model.id
        } else if (outcome is Outcome.Failure) {
            return outcome
        }

        val initialResultsOutcome = remoteDs.getById(id)

        if (initialResultsOutcome is Outcome.Success) {
            localDs.save(initialResultsOutcome.model)
        }

        return initialResultsOutcome
    }

    override suspend fun getById(id: String): Outcome<TModel> {
        val localOutcome = localDs.getById(id)
        if (localOutcome is Outcome.Success) {
            return localOutcome
        }

        return remoteDs.getById(id)
    }

    override suspend fun getUpdatedById(id: String): Outcome<TModel> {
        val outcome = remoteDs.getById(id)

        if (outcome is Outcome.Success) {
            // TODO: Verify if model already exists.
            localDs.update(outcome.model)
        }

        return outcome
    }

    override suspend fun getManyPaged(page: Int): List<TModel> {
        val pageSize = settings.paginationSize
        val localResults = localDs.getMany(page, pageSize)
        if (localResults.size == pageSize) {
            return localResults
        }

        val missingResults = pageSize - localResults.size
        val remoteResults = remoteDs.getMany(page, missingResults)

        return localResults.plus(remoteResults)
    }
}