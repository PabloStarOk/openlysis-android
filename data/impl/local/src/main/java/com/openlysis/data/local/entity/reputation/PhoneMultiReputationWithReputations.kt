package com.openlysis.data.local.entity.reputation

import androidx.room.Embedded
import androidx.room.Relation
import com.openlysis.models.reputation.MultiReputation
import com.openlysis.models.reputation.PhoneNumberReputation

/**
 * Data class representing a [MultiReputationEntity] and its related phone number reputations.
 *
 * @property multiReputation The [MultiReputationEntity] being referenced.
 * @property reputations List of related [PhoneReputationEntity] records.
 */
internal data class PhoneMultiReputationWithReputations(
    @Embedded val multiReputation: MultiReputationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "multiReputationId"
    )
    val reputations: List<PhoneReputationEntity>
) {
    /**
     * Builds a [MultiReputation] model from this entity and its reputations.
     *
     * @return The [MultiReputation] model for [PhoneNumberReputation].
     */
    fun buildMultiReputation(): MultiReputation<PhoneNumberReputation> =
        MultiReputation(
            id = multiReputation.id,
            date = multiReputation.evaluationDate,
            finalVerdict = multiReputation.finalVerdict,
            data = multiReputation.data,
            reputations = reputations.map { r -> r.convertToModel() }
        )
}