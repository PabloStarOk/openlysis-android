package com.openlysis.core.designsystem.components.alert

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.openlysis.core.designsystem.theme.color.AppColorScheme

/**
 * Represents the data associated with a specific alert type for [Alert] component.
 *
 * @property getBackgroundColor A lambda function that returns the background [Color] for the alert, based on the current [AppColorScheme].
 * @property getForegroundColor A lambda function that returns the foreground (text and icon) [Color] for the alert, based on the current [AppColorScheme].
 * @property iconResId The resource ID of the drawable to be used as the alert's icon. Annotated with [DrawableRes] to ensure a valid drawable resource.
 * @property iconAlt The resource ID of the string to be used as the content description for the alert's icon, for accessibility purposes. Annotated with [StringRes] to ensure a valid string resource.
 */
@Immutable
internal data class AlertTypeData(
    val getBackgroundColor: (AppColorScheme) -> Color,
    val getForegroundColor: (AppColorScheme) -> Color,
    @DrawableRes val iconResId: Int,
    @StringRes val iconAlt: Int
)