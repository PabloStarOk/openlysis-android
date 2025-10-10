package com.openlysis.core.network.di

import com.openlysis.core.network.AppDispatcher
import jakarta.inject.Qualifier

/**
 * Qualifier annotation for specifying a particular [AppDispatcher] in dependency injection.
 *
 * @property appDispatcher The dispatcher to be injected.
 */
@Qualifier
annotation class Dispatcher(
    val appDispatcher: AppDispatcher
)