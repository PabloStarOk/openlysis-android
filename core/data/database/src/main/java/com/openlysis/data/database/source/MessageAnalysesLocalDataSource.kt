package com.openlysis.data.database.source

import android.util.Log
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.openlysis.data.database.Debugging
import com.openlysis.data.database.dao.MessageAnalysisDao
import com.openlysis.data.database.di.MessageAnalysisDsState
import com.openlysis.data.database.entity.message.MessageAnalysisEntity
import com.openlysis.data.database.entity.message.MessageAnalysisWithResults
import com.openlysis.data.database.entity.reputation.ReputationDataType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Data source for managing [MessageAnalysis] entities and their related analyses in the local database.
 *
 * Handles saving, updating, and retrieving [MessageAnalysis] records, including their associated
 * [UrlMultiAnalysis], [FileMultiAnalysis], [MultiReputation]<[EmailAddressReputation]>, and [MultiReputation]<[PhoneNumberReputation]>.
 *
 * @param state The [LocalDataSourceState] tracking entity limits.
 * @param messageType The [MessageType] this data source handles.
 * @param urlDs The [RelationalLocalDataSource] for [UrlMultiAnalysis] entities.
 * @param fileDs The [RelationalLocalDataSource] for [FileMultiAnalysis] entities.
 * @param emailDs The [RelationalLocalDataSource] for [MultiReputation]<[EmailAddressReputation]> entities.
 * @param phoneDs The [RelationalLocalDataSource] for [MultiReputation]<[PhoneNumberReputation]> entities.
 * @param analysisDao The [MessageAnalysisDao] for database operations.
 */
internal class MessageAnalysesLocalDataSource
    @Inject
    constructor(
        @MessageAnalysisDsState state: LocalDataSourceState,
        private val messageType: MessageType,
        private val urlDs: RelationalLocalDataSource<UrlMultiAnalysis>,
        private val fileDs: RelationalLocalDataSource<FileMultiAnalysis>,
        private val emailDs: RelationalLocalDataSource<MultiReputation<EmailAddressReputation>>,
        private val phoneDs: RelationalLocalDataSource<MultiReputation<PhoneNumberReputation>>,
        private val analysisDao: MessageAnalysisDao
    ) : LocalDataSource<MessageAnalysis>(
            state,
            queueDao = analysisDao,
            existsDao = analysisDao
        ) {
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
         * Retrieves a [MessageAnalysis] by its unique ID.
         *
         * @param id The unique identifier of the [MessageAnalysis] to retrieve.
         * @return The [MessageAnalysis] object with all related analyses and reputations loaded.
         */
        override suspend fun handleGetById(id: String): MessageAnalysis {
            val messageAnalysisWithResults = analysisDao.getById(id)
            return buildMessageAnalysis(withResults = messageAnalysisWithResults)
        }

        /**
         * Retrieves a paginated list of [MessageAnalysis] records with their related analyses.
         *
         * @param page The page number (zero-based).
         * @param size The number of items per page.
         * @return A list of [MessageAnalysis] records for the specified page.
         */
        override suspend fun handleGetMany(
            page: Int,
            size: Int
        ): List<MessageAnalysis> {
            val analysesWithResults = analysisDao.getMany(page, size, messageType)
            return analysesWithResults.map { m -> buildMessageAnalysis(withResults = m) }
        }

        /**
         * Builds a [MessageAnalysis] object from a [MessageAnalysisWithResults] entity,
         * retrieving all related analyses and reputations from their respective data sources.
         *
         * This function fetches associated [UrlMultiAnalysis], [FileMultiAnalysis],
         * [MultiReputation]<[EmailAddressReputation]>, and [MultiReputation]<[PhoneNumberReputation]>
         * asynchronously, then constructs and returns a fully populated [MessageAnalysis] model.
         *
         * @param withResults The [MessageAnalysisWithResults] entity containing the base analysis and references to related data.
         * @return A [MessageAnalysis] object with all related analyses and reputations loaded.
         */
        private suspend fun buildMessageAnalysis(
            withResults: MessageAnalysisWithResults
        ): MessageAnalysis {
            val messageAnalysisEntity = withResults.messageAnalysis
            var urlMultiAnalyses = listOf<UrlMultiAnalysis>()
            var fileMultiAnalyses = listOf<FileMultiAnalysis>()
            var emailMultiReputations = listOf<MultiReputation<EmailAddressReputation>>()
            var phoneMultiReputations = listOf<MultiReputation<PhoneNumberReputation>>()

            coroutineScope {
                val urlDeferred =
                    async {
                        val ids = withResults.urlMultiAnalyses.map { it.id }.toTypedArray()
                        urlDs.getManyByIds(*ids)
                    }

                val fileDeferred =
                    async {
                        val ids = withResults.fileMultiAnalyses.map { it.id }.toTypedArray()
                        fileDs.getManyByIds(*ids)
                    }

                val emailDeferred =
                    async {
                        val ids =
                            withResults.multiReputations
                                .filter { it.dataType == ReputationDataType.EmailAddress }
                                .map { it.id }
                                .toTypedArray()
                        emailDs.getManyByIds(*ids)
                    }

                val phoneDeferred =
                    async {
                        val ids =
                            withResults.multiReputations
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

            return MessageAnalysis(
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

        companion object {
            private val LOG_TAG = MessageAnalysesLocalDataSource::class.java.simpleName.take(23)
        }
    }