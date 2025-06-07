package com.openlysis.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.openlysis.core.designsystem.theme.color.DarkAppColorScheme
import com.openlysis.core.designsystem.theme.color.LightAppColorScheme
import com.openlysis.core.designsystem.theme.radius.FallbackRadius
import com.openlysis.core.designsystem.theme.type.FallbackTypography

val LocalAppColorScheme =
    staticCompositionLocalOf {
        LightAppColorScheme
    }

@Composable
fun OpenlysisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColorScheme =
        when {
            darkTheme -> DarkAppColorScheme
            else -> LightAppColorScheme
        }

    CompositionLocalProvider(
        LocalAppColorScheme provides appColorScheme,
        content = {
            MaterialTheme(
                typography = FallbackTypography,
                shapes = FallbackRadius,
                content = content
            )
        }
    )
}