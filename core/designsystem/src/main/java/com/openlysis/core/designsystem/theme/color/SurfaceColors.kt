package com.openlysis.core.designsystem.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * A set of colors to use for backgrounds and containers.
 */
@Immutable
data class SurfaceColors(
    val primary: Color,
    val primaryActive: Color,
    val secondary: Color,
    val secondaryActive: Color,
    val tertiary: Color,
    val tertiaryActive: Color
)