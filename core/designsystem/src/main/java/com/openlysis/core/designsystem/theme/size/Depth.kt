package com.openlysis.core.designsystem.theme.size

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * A set of values to be used for elevation and shadow effects.
 */
@Immutable
data class Depth(
    val value0: Dp,
    val value025: Dp,
    val value050: Dp,
    val value100: Dp,
    val value200: Dp,
    val value400: Dp,
    val value800: Dp,
    val value1200: Dp
)