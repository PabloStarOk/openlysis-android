package com.openlysis.core.designsystem.theme.color

import androidx.compose.runtime.Immutable

/**
 * App's color scheme which contains tokenized colors for backgrounds, borders, text and icons.
 */
@Immutable
data class AppColorScheme(
    val background: SemanticColors<SurfaceColors>,
    val border: SemanticColors<PaletteColors>,
    val text: SemanticColors<ForegroundColors>,
    val icon: SemanticColors<ForegroundColors>
)