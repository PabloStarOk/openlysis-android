package com.openlysis.data.database.entity.reputation

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.Reputation
import com.openlysis.data.database.entity.message.MessageAnalysisEntity
import java.time.Instant

/**
 * Entity representing a multi-reputation record for an email address or phone number.
 *
 * @property id Unique identifier for the multi-reputation.
 * @property dataType The [ReputationDataType] (email or phone).
 * @property evaluationDate The date and time when the reputation was evaluated.
 * @property finalVerdict The final [Verdict] of the multi-reputation.
 * @property data The data being evaluated (e.g., email address or phone number).
 * @property messageAnalysisId The ID of the parent message analysis, if any.
 * @property createdAt The timestamp when this entity was created in the database.
 * @property hasParent Whether this multi-reputation has a parent message analysis.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            MessageAnalysisEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageAnalysisId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class MultiReputationEntity(
    @PrimaryKey val id: String,
    val dataType: ReputationDataType,
    val evaluationDate: Instant,
    val finalVerdict: Verdict,
    val data: String,
    val messageAnalysisId: String?,
    val createdAt: Instant,
    val hasParent: Boolean
) {
    /**
     * Converts this entity to a [MultiReputation] model.
     *
     * @param reputations The list of [Reputation] results for this data.
     * @return The [MultiReputation] model.
     */
    fun <TReputation> convertToModel(
        reputations: List<TReputation>
    ): MultiReputation<TReputation>
            where TReputation : Reputation =
        MultiReputation(
            id = id,
            evaluationDate = evaluationDate,
            finalVerdict = finalVerdict,
            data = data,
            reputations = reputations
        )

    companion object {
        /**
         * Creates a [MultiReputationEntity] from a [MultiReputation] model, data type, and parent message analysis ID.
         *
         * @param model The [MultiReputation] model.
         * @param dataType The [ReputationDataType].
         * @param messageAnalysisId The parent message analysis ID, if any.
         * @return The [MultiReputationEntity] entity.
         */
        fun <TReputation> createFromModel(
            model: MultiReputation<TReputation>,
            dataType: ReputationDataType,
            messageAnalysisId: String?
        ): MultiReputationEntity
        where TReputation : Reputation =
            MultiReputationEntity(
                id = model.id,
                dataType = dataType,
                evaluationDate = model.evaluationDate,
                finalVerdict = model.finalVerdict,
                data = model.data,
                messageAnalysisId = messageAnalysisId,
                createdAt = Instant.now(),
                hasParent = messageAnalysisId != null
            )
    }
}