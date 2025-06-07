package com.openlysis.core.designsystem.components.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.openlysis.core.designsystem.theme.color.AppColorScheme

/**
 * Returns the colors to stylize an specific button type.
 */
@Immutable
internal data class ButtonTypeColors(
    val getBackgroundColor: (scheme: AppColorScheme) -> Color,
    val getBorderColor: (scheme: AppColorScheme) -> Color,
    val getForegroundColor: (scheme: AppColorScheme) -> Color,
    val getRippleColor: (scheme: AppColorScheme) -> Color
)