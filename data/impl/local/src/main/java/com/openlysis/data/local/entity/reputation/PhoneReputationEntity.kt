package com.openlysis.data.local.entity.reputation

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openlysis.models.common.Verdict
import com.openlysis.models.reputation.PhoneNumberReputation

/**
 * Entity representing a phone number reputation result from a specific service.
 *
 * @property id Unique identifier for the reputation result.
 * @property serviceName The name of the reputation service.
 * @property verdict The [Verdict] assigned by the service.
 * @property localFormat The local format of the phone number.
 * @property countryCode The country code of the phone number.
 * @property dialingCode The dialing code of the phone number.
 * @property lineType The type of phone line (e.g., mobile, landline).
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
    ]
)
internal data class PhoneReputationEntity(
    @PrimaryKey val id: String,
    val serviceName: String,
    val verdict: Verdict,
    val localFormat: String,
    val countryCode: String,
    val dialingCode: Int,
    val lineType: String,
    val multiReputationId: String
) {
    /**
     * Converts this entity to a [PhoneNumberReputation] model.
     *
     * @return The [PhoneNumberReputation] model.
     */
    fun convertToModel(): PhoneNumberReputation =
        PhoneNumberReputation(
            id = id,
            serviceName = serviceName,
            verdict = verdict,
            localFormat = localFormat,
            countryCode = countryCode,
            dialingCode = dialingCode,
            lineType = lineType
        )

    companion object {
        /**
         * Creates a [PhoneReputationEntity] from a [PhoneNumberReputation] model and parent multi-reputation ID.
         *
         * @param model The [PhoneNumberReputation] model.
         * @param multiReputationId The parent multi-reputation ID.
         * @return The [PhoneReputationEntity] entity.
         */
        fun createFromModel(
            model: PhoneNumberReputation,
            multiReputationId: String
        ): PhoneReputationEntity =
            PhoneReputationEntity(
                id = model.id,
                serviceName = model.serviceName,
                verdict = model.verdict,
                localFormat = model.localFormat,
                countryCode = model.countryCode,
                dialingCode = model.dialingCode,
                lineType = model.lineType,
                multiReputationId = multiReputationId
            )
    }
}