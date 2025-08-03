package com.openlysis.data.analysis.repository

import com.openlysis.core.outcome.Outcome
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.data.analysis.source.AnalysesLocalDataSource
import com.openlysis.data.analysis.source.AnalysesRemoteDataSource
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
        private val localDs: AnalysesLocalDataSource<TModel>,
        private val remoteDs: AnalysesRemoteDataSource<TRequest, TModel>
    ) : AnalysesRepository<TRequest, TModel>
    where TRequest : Any, TModel : Model {
    override suspend fun analyze(request: TRequest): Outcome<TModel> {
        val outcome = remoteDs.analyze(request)
        var id = ""
        if (outcome is Outcome.Success) {
            id = outcome.value.id
        } else if (outcome is Outcome.Failure) {
            return outcome
        }

        val initialResultsOutcome = remoteDs.getById(id)

        if (initialResultsOutcome is Outcome.Success) {
            localDs.save(initialResultsOutcome.value)
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
            val model = outcome.value
            if (localDs.exists(model)) {
                localDs.update(model)
            } else {
                localDs.save(model)
            }
        }

        return outcome
    }

    override suspend fun getManyPaged(
        page: Int,
        pageSize: Int
    ): Outcome<List<TModel>> {
        val localResults = localDs.getMany(page, pageSize)
        if (localResults.size == pageSize) {
            return Outcome.Success(localResults)
        }

        val remoteResultsOutcome = remoteDs.getMany(page, pageSize)

        return if (remoteResultsOutcome is Outcome.Success) {
            val remoteResults = remoteResultsOutcome.value
            val missingAmount = pageSize - localResults.size

            val localIds = localResults.map { it.id }.toSet()
            val additionalResults =
                remoteResults
                    .filterNot { it.id in localIds }
                    .take(missingAmount)

            Outcome.Success(localResults + additionalResults)
        } else if (localResults.isNotEmpty()) {
            Outcome.Success(localResults)
        } else {
            remoteResultsOutcome
        }
    }
}