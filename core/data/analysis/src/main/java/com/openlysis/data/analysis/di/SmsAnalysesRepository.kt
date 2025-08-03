package com.openlysis.data.analysis.di

import jakarta.inject.Qualifier

/**
 * Qualifier annotation for identifying the repository implementation
 * for SMS analyses.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SmsAnalysesRepository