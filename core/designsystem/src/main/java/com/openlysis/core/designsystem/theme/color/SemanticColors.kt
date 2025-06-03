package com.openlysis.core.designsystem.theme.color

import androidx.compose.runtime.Immutable

@Immutable
data class SemanticColors<TColors>(
    val default: TColors,
    val neutral: TColors,
    val brand: TColors,
    val positive: TColors,
    val warning: TColors,
    val danger: TColors,
    val disabled: TColors
) where TColors : Any