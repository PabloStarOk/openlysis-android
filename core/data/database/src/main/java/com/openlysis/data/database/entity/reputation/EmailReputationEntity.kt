package com.openlysis.data.database.entity.reputation

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation

/**
 * Entity representing an email address reputation result from a specific service.
 *
 * @property id Unique identifier for the reputation result.
 * @property serviceName The name of the reputation service.
 * @property verdict The [Verdict] assigned by the service.
 * @property isDisposable Whether the email is disposable, if available.
 * @property isRiskyTld Whether the email uses a risky TLD, if available.
 * @property multiReputationId The ID of the parent multi-reputation entity.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            MultiReputationEntity::class,
            parentColumns = ["id"],
            childColumns = ["multiReputationId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("multiReputationId")]
)
internal data class EmailReputationEntity(
    @PrimaryKey val id: String,
    val serviceName: String,
    val verdict: Verdict,
    val isDisposable: Boolean?,
    val isRiskyTld: Boolean?,
    val multiReputationId: String
) {
    /**
     * Converts this entity to an [EmailAddressReputation] model.
     *
     * @return The [EmailAddressReputation] model.
     */
    fun convertToModel(): EmailAddressReputation =
        EmailAddressReputation(
            id = id,
            serviceName = serviceName,
            verdict = verdict,
            isDisposable = isDisposable,
            isRiskyTld = isRiskyTld
        )

    companion object {
        /**
         * Creates an [EmailReputationEntity] from an [EmailAddressReputation] model and parent multi-reputation ID.
         *
         * @param model The [EmailAddressReputation] model.
         * @param multiReputationId The parent multi-reputation ID.
         * @return The [EmailReputationEntity] entity.
         */
        fun createFromModel(
            model: EmailAddressReputation,
            multiReputationId: String
        ): EmailReputationEntity =
            EmailReputationEntity(
                id = model.id,
                serviceName = model.serviceName,
                verdict = model.verdict,
                isDisposable = model.isDisposable,
                isRiskyTld = model.isRiskyTld,
                multiReputationId = multiReputationId
            )
    }
}