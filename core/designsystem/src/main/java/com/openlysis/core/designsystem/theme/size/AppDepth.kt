package com.openlysis.core.designsystem.theme.size

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalAppDepth =
    staticCompositionLocalOf {
        Depth(
            value0 = 0.dp,
            value025 = 1.dp,
            value050 = 2.dp,
            value100 = 4.dp,
            value200 = 8.dp,
            value400 = 16.dp,
            value800 = 32.dp,
            value1200 = 48.dp
        )
    }