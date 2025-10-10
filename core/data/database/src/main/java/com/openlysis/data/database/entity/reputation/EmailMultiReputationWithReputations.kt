package com.openlysis.data.database.entity.reputation

import androidx.room.Embedded
import androidx.room.Relation
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.database.entity.BuildablePojo

/**
 * Data class representing a [MultiReputationEntity] and its related email address reputations.
 *
 * @property multiReputation The [MultiReputationEntity] being referenced.
 * @property reputations List of related [EmailReputationEntity] records.
 */
internal data class EmailMultiReputationWithReputations(
    @Embedded val multiReputation: MultiReputationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "multiReputationId"
    )
    val reputations: List<EmailReputationEntity>
) : BuildablePojo<MultiReputation<EmailAddressReputation>> {
    /**
     * Builds a [MultiReputation] model from this entity and its reputations.
     *
     * @return The [MultiReputation] model for [EmailAddressReputation].
     */
    override fun buildModel(): MultiReputation<EmailAddressReputation> =
        MultiReputation(
            id = multiReputation.id,
            evaluationDate = multiReputation.evaluationDate,
            finalVerdict = multiReputation.finalVerdict,
            data = multiReputation.data,
            reputations = reputations.map { r -> r.convertToModel() }
        )
}