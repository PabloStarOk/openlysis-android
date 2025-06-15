package com.openlysis.data.local.entity.message

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openlysis.models.analysis.AnalysisStatus
import com.openlysis.models.common.HashValues
import com.openlysis.models.common.Verdict
import com.openlysis.models.message.Message
import com.openlysis.models.message.MessageAnalysis
import java.time.Instant

/**
 * Entity representing a message analysis record in the database.
 *
 * @property id Unique identifier for the message analysis.
 * @property startedDate The date and time when the analysis started.
 * @property message The [Message] being analyzed.
 * @property hashValues Hash values associated with the message.
 * @property status The [AnalysisStatus] of the analysis.
 * @property verdict The [Verdict] of the analysis.
 * @property createdAt The timestamp when this entity was created in the database.
 */
@Entity
internal data class MessageAnalysisEntity(
    @PrimaryKey val id: String,
    val startedDate: Instant,
    @Embedded("message_") val message: Message,
    @Embedded("hash_") val hashValues: HashValues,
    val status: AnalysisStatus,
    val verdict: Verdict,
    val createdAt: Instant
) {
    companion object {
        /**
         * Creates a [MessageAnalysisEntity] from a [MessageAnalysis] model.
         *
         * @param model The [MessageAnalysis] model to convert.
         * @return A new [MessageAnalysisEntity] instance.
         */
        fun createFromModel(model: MessageAnalysis): MessageAnalysisEntity =
            MessageAnalysisEntity(
                id = model.id,
                startedDate = model.startedDate,
                message = model.message,
                hashValues = model.hashValues,
                status = model.status,
                verdict = model.verdict,
                createdAt = Instant.now()
            )
    }
}