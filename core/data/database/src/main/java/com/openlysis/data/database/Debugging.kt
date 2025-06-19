package com.openlysis.data.database

import android.util.Log
import com.openlysis.data.analysis.model.analysis.Analysis
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.analysis.UrlMultiAnalysis
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.reputation.EmailAddressReputation
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.data.analysis.model.reputation.PhoneNumberReputation
import com.openlysis.data.analysis.model.reputation.Reputation

/**
 * Utility object for verbose logging of analysis and reputation models in the local data layer.
 *
 * Provides detailed log output for [MessageAnalysis], [FileMultiAnalysis], [UrlMultiAnalysis],
 * [MultiReputation]<[EmailAddressReputation]>, and [MultiReputation]<[PhoneNumberReputation]>.
 *
 * All log methods check [Log.isLoggable] for [Log.VERBOSE] before outputting.
 */
internal object Debugging {
    /**
     * Logs a detailed, verbose representation of a [MessageAnalysis] and its related analyses.
     *
     * @param tag The log tag.
     * @param contextMsg Contextual message for the log.
     * @param analysis The [MessageAnalysis] to log.
     */
    fun logVerboseMessageAnalysis(
        tag: String?,
        contextMsg: String,
        analysis: MessageAnalysis
    ) {
        if (!Log.isLoggable(tag, Log.VERBOSE)) return

        val logBuilder = StringBuilder()
        logBuilder.appendLine(contextMsg)
        logBuilder.appendLine("\tID: ${analysis.id}")
        logBuilder.appendLine("\tStarted Date: ${analysis.startedDate}")
        logBuilder.appendLine("\tStatus: ${analysis.status}")
        logBuilder.appendLine("\tVerdict ${analysis.verdict}")
        logBuilder.appendLine("\tHash values: ${analysis.hashValues}")
        logBuilder.appendLine("\tUrl Multi Analyses: [")
        logBuilder.appendLine(
            analysis.urlMultiAnalyses
                .joinToString("\n") {
                    getUrlMultiAnalysisLog(it)
                }.prependIndent("\t")
        )
        logBuilder.appendLine("\t]")
        logBuilder.appendLine("\tFile Multi Analyses: [")
        logBuilder.appendLine(
            analysis.fileMultiAnalyses
                .joinToString("\n") {
                    getFileMultiAnalysisLog(it)
                }.prependIndent("\t")
        )
        logBuilder.appendLine("\t]")
        logBuilder.appendLine("\tEmail Multi Reputations: [")
        logBuilder.appendLine(
            analysis.emailAddressMultiReputations
                .joinToString("\n") {
                    getMultiReputationLog(it)
                }.prependIndent("\t")
        )
        logBuilder.appendLine("\t]")
        logBuilder.appendLine("\tPhone Multi Reputations: [")
        logBuilder.appendLine(
            analysis.phoneNumberMultiReputations
                .joinToString("\n") {
                    getMultiReputationLog(it)
                }.prependIndent("\t")
        )
        logBuilder.appendLine("\t]")
        Log.println(
            Log.VERBOSE,
            tag,
            logBuilder.toString()
        )
    }

    /**
     * Logs a detailed, verbose representation of a [FileMultiAnalysis].
     *
     * @param tag The log tag.
     * @param contextMsg Contextual message for the log.
     * @param analysis The [FileMultiAnalysis] to log.
     */
    fun logVerboseFileMultiAnalysis(
        tag: String?,
        contextMsg: String,
        analysis: FileMultiAnalysis
    ) {
        if (!Log.isLoggable(tag, Log.VERBOSE)) return
        Log.println(
            Log.VERBOSE,
            tag,
            contextMsg + "\n${getFileMultiAnalysisLog(analysis)}"
        )
    }

    /**
     * Logs a detailed, verbose representation of a [UrlMultiAnalysis].
     *
     * @param tag The log tag.
     * @param contextMsg Contextual message for the log.
     * @param analysis The [UrlMultiAnalysis] to log.
     */
    fun logVerboseUrlMultiAnalysis(
        tag: String?,
        contextMsg: String,
        analysis: UrlMultiAnalysis
    ) {
        if (!Log.isLoggable(tag, Log.VERBOSE)) return
        Log.println(
            Log.VERBOSE,
            tag,
            contextMsg + "\n${getUrlMultiAnalysisLog(analysis)}"
        )
    }

    /**
     * Logs a detailed, verbose representation of a [MultiReputation] and its reputations.
     *
     * @param tag The log tag.
     * @param contextMsg Contextual message for the log.
     * @param reputation The [MultiReputation] to log.
     */
    fun logVerboseMultiReputation(
        tag: String?,
        contextMsg: String,
        reputation: MultiReputation<*>
    ) {
        if (!Log.isLoggable(tag, Log.VERBOSE)) return
        Log.println(
            Log.VERBOSE,
            tag,
            contextMsg + "\n${getMultiReputationLog(reputation)}"
        )
    }

    /**
     * Returns a detailed string representation of a [FileMultiAnalysis], including its analyses.
     *
     * @param analysis The [FileMultiAnalysis] to describe.
     * @return A string representation for logging.
     */
    private fun getFileMultiAnalysisLog(analysis: FileMultiAnalysis): String {
        val analysisLogBuilder = StringBuilder()
        analysisLogBuilder.appendLine("\tID: ${analysis.id}")
        analysisLogBuilder.appendLine("\tStarted Date: ${analysis.startedDate}")
        analysisLogBuilder.appendLine("\tStatus: ${analysis.status}")
        analysisLogBuilder.appendLine("\tFinal verdict: ${analysis.finalVerdict}")
        analysisLogBuilder.appendLine("\tAverage threat score: ${analysis.avgThreatScore}")
        analysisLogBuilder.appendLine("\tHash values: ${analysis.hashValues}")
        analysisLogBuilder.appendLine("\tFile metadata: ${analysis.fileMetadata}")
        analysisLogBuilder.appendLine("\tAnalyses: [")
        analysis.analyses.forEach { a ->
            analysisLogBuilder.appendLine(getAnalysisLog(a).prependIndent("\t\t"))
        }
        analysisLogBuilder.appendLine("\t]")
        return analysisLogBuilder.toString()
    }

    /**
     * Returns a detailed string representation of an [Analysis].
     *
     * @param analysis The [Analysis] to describe.
     * @return A string representation for logging.
     */
    private fun getAnalysisLog(analysis: Analysis): String {
        val analysisLogBuilder = StringBuilder()
        analysisLogBuilder.appendLine("Service Name: ${analysis.serviceName}")
        analysisLogBuilder.appendLine("Status: ${analysis.status}")
        analysisLogBuilder.appendLine("Verdict: ${analysis.verdict}")
        analysisLogBuilder.appendLine("Threat Score: ${analysis.threatScore}")
        return analysisLogBuilder.toString()
    }

    /**
     * Returns a detailed string representation of a [UrlMultiAnalysis], including its analyses.
     *
     * @param analysis The [UrlMultiAnalysis] to describe.
     * @return A string representation for logging.
     */
    private fun getUrlMultiAnalysisLog(analysis: UrlMultiAnalysis): String {
        val analysisLogBuilder = StringBuilder()
        analysisLogBuilder.appendLine("\tID: ${analysis.id}")
        analysisLogBuilder.appendLine("\tStarted Date: ${analysis.startedDate}")
        analysisLogBuilder.appendLine("\tStatus: ${analysis.status}")
        analysisLogBuilder.appendLine("\tFinal verdict: ${analysis.finalVerdict}")
        analysisLogBuilder.appendLine("\tAverage threat score: ${analysis.avgThreatScore}")
        analysisLogBuilder.appendLine("\tHash values: ${analysis.hashValues}")
        analysisLogBuilder.appendLine("\tURL: ${analysis.url}")
        analysisLogBuilder.appendLine("\tAnalyses: [")
        analysis.analyses.forEach { a ->
            analysisLogBuilder.appendLine(getAnalysisLog(a).prependIndent("\t\t"))
        }
        analysisLogBuilder.appendLine("\t]")
        return analysisLogBuilder.toString()
    }

    /**
     * Returns a detailed string representation of a [MultiReputation] and its reputations.
     *
     * @param reputation The [MultiReputation] to describe.
     * @return A string representation for logging.
     */
    private fun getMultiReputationLog(reputation: MultiReputation<*>): String {
        val repLogBuilder = StringBuilder()
        repLogBuilder.appendLine("\tID: ${reputation.id}")
        repLogBuilder.appendLine("\tDate: ${reputation.date}")
        repLogBuilder.appendLine("\tFinal verdict: ${reputation.finalVerdict}")
        repLogBuilder.appendLine("\tData: ${reputation.data}")
        repLogBuilder.appendLine("\tReputations: [")
        reputation.reputations.forEach { r ->
            repLogBuilder.appendLine(getReputationLog(r).prependIndent("\t\t"))
        }
        repLogBuilder.appendLine("\t]")
        return repLogBuilder.toString()
    }

    /**
     * Returns a detailed string representation of a [Reputation] (email or phone).
     *
     * @param reputation The [Reputation] to describe.
     * @return A string representation for logging.
     */
    private fun getReputationLog(reputation: Reputation): String {
        val sb = StringBuilder()
        when (reputation) {
            is EmailAddressReputation -> {
                sb.appendLine("EmailAddressReputation:")
                sb.appendLine("\tService Name: ${reputation.serviceName}")
                sb.appendLine("\tVerdict: ${reputation.verdict}")
                sb.appendLine("\tIs Disposable: ${reputation.isDisposable}")
                sb.appendLine("\tIs Risky TLD: ${reputation.isRiskyTld}")
            }
            is PhoneNumberReputation -> {
                sb.appendLine("PhoneNumberReputation:")
                sb.appendLine("\tService Name: ${reputation.serviceName}")
                sb.appendLine("\tVerdict: ${reputation.verdict}")
                sb.appendLine("\tLocal Format: ${reputation.localFormat}")
                sb.appendLine("\tCountry Code: ${reputation.countryCode}")
                sb.appendLine("\tDialing Code: ${reputation.dialingCode}")
                sb.appendLine("\tLine Type: ${reputation.lineType}")
            }
            else -> {
                sb.appendLine(reputation.toString())
            }
        }
        return sb.toString().trimEnd()
    }
}