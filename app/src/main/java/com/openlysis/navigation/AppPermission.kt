package com.openlysis.navigation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Enum representing the permissions used in the application.
 *
 * @property manifestName The name of the permission as defined in the Android manifest.
 */
internal enum class AppPermission(
    val manifestName: String
) {
    /**
     * Permission to receive SMS messages.
     */
    ReceiveSms(manifestName = Manifest.permission.RECEIVE_SMS),

    /**
     * Permission to post notifications (Android 13+).
     */
    @RequiresApi(value = Build.VERSION_CODES.TIRAMISU)
    PostNotifications(manifestName = Manifest.permission.POST_NOTIFICATIONS)
}