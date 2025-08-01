package com.openlysis.core.link

/**
 * Contains constants and utilities for building deep links within the Openlysis app.
 */
object DeepLinks {
    private const val SCHEME_AND_HOST = "https://www.openlysis.com"

    /** The fully qualified name of the main activity in the Openlysis app. */
    const val OPENLYSIS_ACTIVITY_NAME = "com.openlysis.MainActivity"

    /*
     * Constants related to tool screens.
     */
    object Tools {
        /**
         * Provides constants and helper functions for SMS analysis tool deep links.
         */
        object Sms {
            private const val ENCODED_CONTENT_QUERY_PARAM = "content"
            private const val PATH = "sms-analysis"
            private const val BASE_PATH = "$SCHEME_AND_HOST/$PATH"

            /** Key for the encoded sender in the deep link. */
            const val ENCODED_SENDER_KEY = "sender"

            /** Key for the encoded content in the deep link. */
            const val ENCODED_CONTENT_KEY = "content"

            /** URI pattern for SMS analysis tool deep links. */
            const val URI_PATTERN =
                "$BASE_PATH/{$ENCODED_SENDER_KEY}?${ENCODED_CONTENT_QUERY_PARAM}={$ENCODED_CONTENT_KEY}"

            /**
             * Creates a deep link URI to access the SMS analysis tool with a predefined sender and content.
             *
             * @param sender The encoded sender value.
             * @param content The encoded content value.
             * @return A URI string that can be used to access the SMS analysis tool.
             */
            fun createUri(
                sender: String,
                content: String
            ): String = "$BASE_PATH/%s?${ENCODED_CONTENT_QUERY_PARAM}=%s".format(sender, content)
        }
    }

    /*
     * Constants related to result screens.
     */
    object Results {
        /**
         * Provides constants and helper functions for message analysis result deep links.
         */
        object Message {
            private const val PATH = "message-results"
            private const val BASE_PATH = "$SCHEME_AND_HOST/$PATH"

            /** Key for the message type in the message analysis result deep link. */
            const val MESSAGE_TYPE_KEY = "messageType"

            /** Key for the message analysis result ID in the deep link. */
            const val ANALYSIS_ID_KEY = "analysisId"

            /** URI pattern for message analysis result deep links. */
            const val URI_PATTERN = "$BASE_PATH/{$MESSAGE_TYPE_KEY}/{$ANALYSIS_ID_KEY}"

            /**
             * Creates a deep link URI to access the message analysis result screen with a specific analysis ID.
             *
             * @param analysisId The unique identifier for the message analysis result.
             * @return A URI string that can be used to access the message analysis result.
             */
            fun createUri(
                messageType: String,
                analysisId: String
            ): String = "$BASE_PATH/%s/%s".format(messageType, analysisId)
        }
    }
}