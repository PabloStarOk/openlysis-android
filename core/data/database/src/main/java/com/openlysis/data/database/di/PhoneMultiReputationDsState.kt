package com.openlysis.data.database.di

import javax.inject.Qualifier

/**
 * Qualifier of a [com.openlysis.data.local.source.LocalDataSourceState] singleton instance for [com.openlysis.data.local.source.PhoneMultiReputationDataSource].
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PhoneMultiReputationDsState