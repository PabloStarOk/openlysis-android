package com.openlysis

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Openlysis.
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation, including a base class for dependency injection.
 */
@HiltAndroidApp
class OpenlysisApp : Application()