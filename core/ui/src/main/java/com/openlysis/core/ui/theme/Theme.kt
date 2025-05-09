package com.openlysis.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.openlysis.core.ui.theme.type.Typography

private val DarkColorScheme =
    darkColorScheme(
        primary = BrandTransparent.Value300,
        primaryContainer = BrandTransparent.Value300,
        surface = BrandTransparent.Value100,
        onSurface = Brand.Value200,
        background = Gray.Value900,
        onBackground = Color.White,
        outline = Gray.Value600,
        onSurfaceVariant = White.Value500
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Brand.Value800,
        primaryContainer = Brand.Value800,
        surface = Brand.Value100,
        onSurface = Brand.Value800,
        background = Color.White,
        onBackground = Gray.Value900,
        outline = Gray.Value300,
        onSurfaceVariant = Gray.Value500
    )

@Composable
fun OpenlysisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes
    )
}