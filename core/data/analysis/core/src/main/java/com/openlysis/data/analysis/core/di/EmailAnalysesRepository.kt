package com.openlysis.data.analysis.core.di

import jakarta.inject.Qualifier

/**
 * Qualifier annotation for identifying the repository implementation
 * for email analyses.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EmailAnalysesRepository