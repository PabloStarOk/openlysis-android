package com.openlysis.data.analysis.model.message

import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.common.HashValues
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import java.time.Instant

/**
 * Represents the comprehensive analysis of a message.
 *
 * @property id A unique identifier for this specific message analysis.
 * @property startedDate The exact [Instant] when the analysis of the message started.
 * @property message The original [Message] object that was analyzed.
 * @property hashValues A [HashValues] object containing various calculated hashes of the whole [Message].
 * @property status The current [AnalysisStatus] of the message analysis.
 * @property verdict The final [Verdict] reached by the analysis.
 * @property fileMultiAnalyses A list of [FileMultiAnalysis] objects, representing the analysis results for each file attached to the message.
 * @property urlMultiAnalyses A list of [UrlMultiAnalysis] objects, representing the analysis results for each URL found within the message.
 * @property emailAddressMultiReputations A list of [MultiReputation] objects, each containing the reputation analyses for email addresses found in the message.
 * @property phoneNumberMultiReputations A list of [MultiReputation] objects, each containing the reputation analyses for phone numbers found in the message.
 */
data class MessageAnalysis(
    val id: String,
    val startedDate: Instant,
    val message: Message,
    val hashValues: HashValues,
    val status: AnalysisStatus,
    val verdict: Verdict,
    val fileMultiAnalyses: List<FileMultiAnalysis>,
    val urlMultiAnalyses: List<UrlMultiAnalysis>,
    val emailAddressMultiReputations: List<MultiReputation<EmailAddressReputation>>,
    val phoneNumberMultiReputations: List<MultiReputation<PhoneNumberReputation>>
)