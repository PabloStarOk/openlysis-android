package com.openlysis.data.local.di

import javax.inject.Qualifier

/**
 * Qualifier of a [com.openlysis.data.local.source.LocalDataSourceState] singleton instance for [com.openlysis.data.local.source.UrlMultiAnalysisDataSource].
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class UrlMultiAnalysisDsState