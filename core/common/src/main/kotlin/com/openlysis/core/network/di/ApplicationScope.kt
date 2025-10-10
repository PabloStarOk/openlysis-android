package com.openlysis.core.network.di

import javax.inject.Qualifier

/**
 * Qualifier annotation for app's coroutine scope.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope