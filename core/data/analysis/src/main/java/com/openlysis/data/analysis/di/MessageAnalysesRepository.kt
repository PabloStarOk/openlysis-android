package com.openlysis.data.analysis.di

import com.openlysis.data.analysis.model.message.MessageType
import jakarta.inject.Qualifier

/**
 * Qualifier annotation for injecting different implementations of MessageAnalysesRepository
 * based on the provided MessageType.
 *
 * @property type The type of message for which the repository is provided.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MessageAnalysesRepository(
    val type: MessageType
)