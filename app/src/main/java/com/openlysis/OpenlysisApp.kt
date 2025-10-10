package com.openlysis

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class for Openlysis.
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation, including a base class for dependency injection.
 */
@HiltAndroidApp
class OpenlysisApp :
    Application(),
    Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration get() =
        Configuration
            .Builder()
            .setWorkerFactory(workerFactory)
            .build()
}