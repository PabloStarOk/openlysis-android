package com.openlysis.data.local.source

import android.util.Log
import com.openlysis.data.local.Debugging
import com.openlysis.data.local.dao.MessageAnalysisDao
import com.openlysis.data.local.entity.message.MessageAnalysisEntity
import com.openlysis.data.local.entity.reputation.ReputationDataType
import com.openlysis.models.analysis.FileMultiAnalysis
import com.openlysis.models.analysis.UrlMultiAnalysis
import com.openlysis.models.message.MessageAnalysis
import com.openlysis.models.reputation.EmailAddressReputation
import com.openlysis.models.reputation.MultiReputation
import com.openlysis.models.reputation.PhoneNumberReputation
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Data source for managing [MessageAnalysis] entities and their related analyses in the local database.
 *
 * Handles saving, updating, and retrieving [MessageAnalysis] records, including their associated
 * [UrlMultiAnalysis], [FileMultiAnalysis], [MultiReputation]<[EmailAddressReputation]>, and [MultiReputation]<[PhoneNumberReputation]>.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param urlDs The [RelationalLocalDataSource] for [UrlMultiAnalysis] entities.
 * @param fileDs The [RelationalLocalDataSource] for [FileMultiAnalysis] entities.
 * @param emailDs The [RelationalLocalDataSource] for [MultiReputation]<[EmailAddressReputation]> entities.
 * @param phoneDs The [RelationalLocalDataSource] for [MultiReputation]<[PhoneNumberReputation]> entities.
 * @param analysisDao The [MessageAnalysisDao] for database operations.
 */
internal class MessageAnalysisDataSource(
    state: LocalDataSourceState,
    private val urlDs: RelationalLocalDataSource<UrlMultiAnalysis>,
    private val fileDs: RelationalLocalDataSource<FileMultiAnalysis>,
    private val emailDs: RelationalLocalDataSource<MultiReputation<EmailAddressReputation>>,
    private val phoneDs: RelationalLocalDataSource<MultiReputation<PhoneNumberReputation>>,
    private val analysisDao: MessageAnalysisDao
) : LocalDataSource<MessageAnalysis>(
        state,
        queueDao = analysisDao
    ) {
    companion object {
        private val LOG_TAG = MessageAnalysisDataSource::class.java.simpleName
    }

    /**
     * Saves a [MessageAnalysis] and its related analyses to the local database.
     *
     * @param model The [MessageAnalysis] to save.
     */
    override suspend fun handleSave(model: MessageAnalysis) {
        val messageAnalysisEntity = MessageAnalysisEntity.createFromModel(model)

        analysisDao.add(messageAnalysisEntity)
        urlDs.save(
            parentId = model.id,
            models = model.urlMultiAnalyses
        )
        fileDs.save(
            parentId = model.id,
            models = model.fileMultiAnalyses
        )
        emailDs.save(
            parentId = model.id,
            models = model.emailAddressMultiReputations
        )
        phoneDs.save(
            parentId = model.id,
            models = model.phoneNumberMultiReputations
        )

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(
                LOG_TAG,
                "${MessageAnalysis::class.java.simpleName} added."
            )
        }

        Debugging.logVerboseMessageAnalysis(
            LOG_TAG,
            contextMsg = "${MessageAnalysis::class.java.simpleName} added.",
            model
        )
    }

    /**
     * Updates a [MessageAnalysis] and its related analyses in the local database.
     *
     * @param model The [MessageAnalysis] to update.
     */
    override suspend fun handleUpdate(model: MessageAnalysis) {
        val entity = MessageAnalysisEntity.createFromModel(model)

        analysisDao.update(entity)
        urlDs.update(
            parentId = model.id,
            models = model.urlMultiAnalyses
        )
        fileDs.update(
            parentId = model.id,
            models = model.fileMultiAnalyses
        )
        emailDs.update(
            parentId = model.id,
            models = model.emailAddressMultiReputations
        )
        phoneDs.update(
            parentId = model.id,
            models = model.phoneNumberMultiReputations
        )

        if (Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(
                LOG_TAG,
                "${MessageAnalysis::class.java.simpleName} updated with ID ${model.id}."
            )
        }

        Debugging.logVerboseMessageAnalysis(
            LOG_TAG,
            contextMsg = "${MessageAnalysis::class.java.simpleName} updated.",
            model
        )
    }

    /**
     * Retrieves a paginated list of [MessageAnalysis] records with their related analyses.
     *
     * @param page The page number (zero-based).
     * @param size The number of items per page.
     * @return A list of [MessageAnalysis] records for the specified page.
     */
    override suspend fun getMany(
        page: Int,
        size: Int
    ): List<MessageAnalysis> {
        val analysesWithResults = analysisDao.getMany(page, size)
        return analysesWithResults.map { m ->
            val messageAnalysisEntity = m.messageAnalysis
            var urlMultiAnalyses = listOf<UrlMultiAnalysis>()
            var fileMultiAnalyses = listOf<FileMultiAnalysis>()
            var emailMultiReputations = listOf<MultiReputation<EmailAddressReputation>>()
            var phoneMultiReputations = listOf<MultiReputation<PhoneNumberReputation>>()

            coroutineScope {
                val urlDeferred =
                    async {
                        val ids = m.urlMultiAnalyses.map { it.id }.toTypedArray()
                        urlDs.getManyByIds(*ids)
                    }

                val fileDeferred =
                    async {
                        val ids = m.fileMultiAnalyses.map { it.id }.toTypedArray()
                        fileDs.getManyByIds(*ids)
                    }

                val emailDeferred =
                    async {
                        val ids =
                            m.multiReputations
                                .filter { it.dataType == ReputationDataType.EmailAddress }
                                .map { it.id }
                                .toTypedArray()
                        emailDs.getManyByIds(*ids)
                    }

                val phoneDeferred =
                    async {
                        val ids =
                            m.multiReputations
                                .filter { it.dataType == ReputationDataType.PhoneNumber }
                                .map { it.id }
                                .toTypedArray()
                        phoneDs.getManyByIds(*ids)
                    }

                urlMultiAnalyses = urlDeferred.await()
                fileMultiAnalyses = fileDeferred.await()
                emailMultiReputations = emailDeferred.await()
                phoneMultiReputations = phoneDeferred.await()
            }

            MessageAnalysis(
                id = messageAnalysisEntity.id,
                startedDate = messageAnalysisEntity.startedDate,
                message = messageAnalysisEntity.message,
                hashValues = messageAnalysisEntity.hashValues,
                status = messageAnalysisEntity.status,
                verdict = messageAnalysisEntity.verdict,
                urlMultiAnalyses = urlMultiAnalyses,
                fileMultiAnalyses = fileMultiAnalyses,
                emailAddressMultiReputations = emailMultiReputations,
                phoneNumberMultiReputations = phoneMultiReputations
            )
        }
    }

    /**
     * Checks if a [MessageAnalysis] exists in the local database.
     *
     * @param model The [MessageAnalysis] to check.
     * @return `true` if the entity exists, `false` otherwise.
     */
    override suspend fun exists(model: MessageAnalysis): Boolean = analysisDao.exists(model.id)
}