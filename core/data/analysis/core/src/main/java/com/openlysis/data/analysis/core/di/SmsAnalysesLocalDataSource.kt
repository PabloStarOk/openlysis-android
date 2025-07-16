package com.openlysis.data.analysis.core.di

import javax.inject.Qualifier

/**
 * Qualifier annotation for distinguishing the local data source implementation of SMS message analyses in dependency injection.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SmsAnalysesLocalDataSource