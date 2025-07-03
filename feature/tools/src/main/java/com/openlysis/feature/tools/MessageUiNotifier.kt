package com.openlysis.feature.tools

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Notifies the user with UI messages.
 *
 * @param context The context used to display messages.
 */
internal class MessageUiNotifier(
    val context: Context
) {
    /**
     * Displays a UI message to the user.
     *
     * @param messageResId The string resource ID of the message to display.
     * @param args Optional arguments to format the message string.
     *
     * TODO: Add error mapping using objects/enums.
     * TODO: Replace with SnackBar for better UX.
     */
    fun showMessage(
        @StringRes messageResId: Int,
        vararg args: Any
    ) {
        val msg = context.getString(messageResId, *args)
        val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast.show()
    }
}