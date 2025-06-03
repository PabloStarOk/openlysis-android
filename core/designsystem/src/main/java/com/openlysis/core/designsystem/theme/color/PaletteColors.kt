package com.openlysis.core.designsystem.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Represents a set of primary, secondary, and tertiary colors for a UI theme.
 */
@Immutable
data class PaletteColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color
)