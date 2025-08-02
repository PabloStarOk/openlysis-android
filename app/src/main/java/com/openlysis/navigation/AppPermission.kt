package com.openlysis.navigation

import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Enum representing the permissions used in the application.
 */
internal enum class AppPermission {
    /**
     * Permission to receive SMS messages.
     */
    ReceiveSms,

    /**
     * Permission to post notifications (Android 13+).
     */
    @RequiresApi(value = Build.VERSION_CODES.TIRAMISU)
    PostNotifications
}