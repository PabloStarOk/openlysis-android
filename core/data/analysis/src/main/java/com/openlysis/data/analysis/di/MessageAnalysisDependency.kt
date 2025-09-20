package com.openlysis.data.analysis.di

import com.openlysis.data.analysis.model.message.MessageType
import jakarta.inject.Qualifier

/**
 * Qualifier annotation for injecting different dependencies for message analyses based on [MessageType].
 *
 * @property type The type of message for which the dependency is provided.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MessageAnalysisDependency(
    val type: MessageType
)