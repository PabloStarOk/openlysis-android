package com.openlysis.data.analysis.di

import com.openlysis.data.analysis.model.message.MessageType
import javax.inject.Qualifier

/**
 * Qualifier annotation for tracking message analysis updates.
 *
 * @property type The type of message to track.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MessageAnalysisUpdateTracker(
    val type: MessageType
)