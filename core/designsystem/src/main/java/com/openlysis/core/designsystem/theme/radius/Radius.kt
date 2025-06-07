package com.openlysis.core.designsystem.theme.radius

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Radius values used for the application.
 *
 * @property value100 A small radius value, often used for subtle rounding.
 * @property value200 A medium radius value.
 * @property value400 A larger radius value.
 * @property full A radius value that typically results in a fully rounded shape (e.g., a circle or pill shape),
 *                often calculated as half of the smaller dimension of the component it's applied to.
 */
@Immutable
data class Radius(
    val value100: Dp,
    val value200: Dp,
    val value400: Dp,
    val full: Dp
)