package com.openlysis.core.designsystem.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ForegroundColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color
)