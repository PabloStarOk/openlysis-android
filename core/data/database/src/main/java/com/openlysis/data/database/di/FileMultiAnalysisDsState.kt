package com.openlysis.data.database.di

import javax.inject.Qualifier

/**
 * Qualifier of a [com.openlysis.data.local.source.LocalDataSourceState] singleton instance for [com.openlysis.data.local.source.FileMultiAnalysisDataSource].
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class FileMultiAnalysisDsState