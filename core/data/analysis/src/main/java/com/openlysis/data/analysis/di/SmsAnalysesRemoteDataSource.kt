package com.openlysis.data.analysis.di

import javax.inject.Qualifier

/**
 * Qualifier annotation for identifying the remote data source implementation
 * for SMS analyses.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SmsAnalysesRemoteDataSource