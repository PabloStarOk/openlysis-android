package com.openlysis.notification.constant

/**
 * Contains constants and types related to SMS analysis notifications.
 */
internal object SmsAnalysis {
    /**
     * Intent action indicating that an SMS analysis is available to be analyzed.
     */
    const val SMS_ANALYSIS_AVAILABLE_INTENT = "com.openlysis.Analysis.SMS_ANALYSIS_AVAILABLE"

    /**
     * Extra key for the notification ID associated with the SMS analysis.
     */
    const val EXTRA_NOTIFICATION_ID =
        "com.openlysis.Analysis.SMS_ANALYSIS_AVAILABLE_NOTIFICATION_ID"

    /**
     * Extra key for the sub-action associated with the SMS analysis.
     */
    const val EXTRA_SUB_ACTION = "com.openlysis.Analysis.SMS_ANALYSIS_AVAILABLE_SUB_ACTION"

    /**
     * Extra key for the SMS PDU.
     */
    const val EXTRA_SMS_PDU = "com.openlysis.Analysis.SMS_ANALYSIS_PDU"

    /**
     * Extra key for the SMS format.
     */
    const val EXTRA_SMS_FORMAT = "com.openlysis.Analysis.SMS_ANALYSIS_FORMAT"

    /**
     * Sub-actions for SMS analysis notifications.
     */
    internal enum class SubAction {
        /**
         * Indicates that the SMS should be analyzed.
         */
        Analyze,

        /**
         * Indicates that the notification to analyze the SMS should be canceled.
         */
        Cancel
    }
}