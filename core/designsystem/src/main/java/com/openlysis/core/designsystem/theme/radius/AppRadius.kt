package com.openlysis.core.designsystem.theme.radius

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

internal val AppRadius =
    Radius(
        value100 = 4.dp,
        value200 = 8.dp,
        value400 = 16.dp,
        full = 9999.dp
    )

/**
 * Default typography of application.
 */
val LocalAppRadius =
    staticCompositionLocalOf {
        AppRadius
    }

/**
 * Contains a material design shapes instance as fallback (just in case).
 */
internal val FallbackRadius =
    Shapes(
        extraSmall = RoundedCornerShape(AppRadius.value100),
        small = RoundedCornerShape(AppRadius.value100),
        medium = RoundedCornerShape(AppRadius.value200),
        large = RoundedCornerShape(AppRadius.value400),
        extraLarge = RoundedCornerShape(AppRadius.full)
    )