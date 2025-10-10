package com.openlysis.data.remote.signalr

/**
 * Enum representing the available SignalR hub methods for receiving analysis updates.
 *
 * - ReceiveEmailAnalysisUpdate: Receives updates for email analysis.
 * - ReceiveSmsAnalysisUpdate: Receives updates for SMS analysis.
 * - ReceiveFileMultiAnalysisUpdate: Receives updates for file multi-analysis.
 * - ReceiveUrlMultiAnalysisUpdate: Receives updates for URL multi-analysis.
 */
internal enum class SignalRHubMethod {
    ReceiveEmailAnalysisUpdate,
    ReceiveSmsAnalysisUpdate,
    ReceiveFileMultiAnalysisUpdate,
    ReceiveUrlMultiAnalysisUpdate
}