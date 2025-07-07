package com.openlysis.data.analysis.core.di

import javax.inject.Qualifier

/**
 * Qualifier annotation for identifying the remote data source implementation
 * for email analyses.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EmailAnalysesRemoteDataSource