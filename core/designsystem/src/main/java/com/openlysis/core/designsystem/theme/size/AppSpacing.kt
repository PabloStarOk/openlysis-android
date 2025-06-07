package com.openlysis.core.designsystem.theme.size

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalAppSpacing =
    staticCompositionLocalOf {
        Spacing(
            value0 = 0.dp,
            value050 = 2.dp,
            value100 = 4.dp,
            value150 = 6.dp,
            value200 = 8.dp,
            value300 = 12.dp,
            value400 = 16.dp,
            value600 = 24.dp,
            value800 = 32.dp,
            value1200 = 48.dp,
            value1600 = 64.dp,
            value2400 = 96.dp,
            value4000 = 160.dp
        )
    }