package com.openlysis.core.designsystem.theme.color

import androidx.compose.ui.graphics.Color

internal val LightBackground =
    SemanticColors<SurfaceColors>(
        default =
            SurfaceColors(
                primary = Color(0xFFFFFFFF),
                primaryActive = Color(0xFFF5F5F5),
                secondary = Color(0xFFF5F5F5),
                secondaryActive = Color(0xFFE6E6E6),
                tertiary = Color(0xFFD9D9D9),
                tertiaryActive = Color(0xFFB3B3B3)
            ),
        neutral =
            SurfaceColors(
                primary = Color(0xFF5A5A5A),
                primaryActive = Color(0xFF434343),
                secondary = Color(0xFFCDCDCD),
                secondaryActive = Color(0xFFB2B2B2),
                tertiary = Color(0xFFE3E3E3),
                tertiaryActive = Color(0xFFCDCDCD)
            ),
        brand =
            SurfaceColors(
                primary = Color(0xFF106FEB),
                primaryActive = Color(0xFF0562DD),
                secondary = Color(0xFFBFDCFF),
                secondaryActive = Color(0xFF94C5FF),
                tertiary = Color(0xFFEEF6FF),
                tertiaryActive = Color(0xFFBFDCFF)
            ),
        positive =
            SurfaceColors(
                primary = Color(0xFF14AE5C),
                primaryActive = Color(0xFF009951),
                secondary = Color(0xFFCFF7D3),
                secondaryActive = Color(0xFFAFF4C6),
                tertiary = Color(0xFFEBFFEE),
                tertiaryActive = Color(0xFFCFF7D3)
            ),
        warning =
            SurfaceColors(
                primary = Color(0xFFE8B931),
                primaryActive = Color(0xFFC3991E),
                secondary = Color(0xFFFFF1C2),
                secondaryActive = Color(0xFFFFE8A3),
                tertiary = Color(0xFFFFFBEB),
                tertiaryActive = Color(0xFFFFF1C2)
            ),
        danger =
            SurfaceColors(
                primary = Color(0xFFEC221F),
                primaryActive = Color(0xFFC00F0C),
                secondary = Color(0xFFFDD3D0),
                secondaryActive = Color(0xFFFCB3AD),
                tertiary = Color(0xFFFEE9E7),
                tertiaryActive = Color(0xFFFDD3D0)
            ),
        disabled =
            SurfaceColors(
                primary = Color(0xFFD9D9D9),
                primaryActive = Color.Unspecified,
                secondary = Color.Unspecified,
                secondaryActive = Color.Unspecified,
                tertiary = Color.Unspecified,
                tertiaryActive = Color.Unspecified
            )
    )

internal val LightBorder =
    SemanticColors<PaletteColors>(
        default =
            PaletteColors(
                primary = Color(0xFFD9D9D9),
                secondary = Color(0xFF757575),
                tertiary = Color(0xFF383838)
            ),
        neutral =
            PaletteColors(
                primary = Color(0xFF303030),
                secondary = Color(0xFF767676),
                tertiary = Color(0xFFB2B2B2)
            ),
        brand =
            PaletteColors(
                primary = Color(0xFF0562DD),
                secondary = Color(0xFF418DF2),
                tertiary = Color(0xFF569EFB)
            ),
        positive =
            PaletteColors(
                primary = Color(0xFF02542D),
                secondary = Color(0xFF009951),
                tertiary = Color(0xFF14AE5C)
            ),
        warning =
            PaletteColors(
                primary = Color(0xFF463A16),
                secondary = Color(0xFF755E1B),
                tertiary = Color(0xFF97771D)
            ),
        danger =
            PaletteColors(
                primary = Color(0xFF900B09),
                secondary = Color(0xFFC00F0C),
                tertiary = Color(0xFFEC221F)
            ),
        disabled =
            PaletteColors(
                primary = Color(0xFFB3B3B3),
                secondary = Color.Unspecified,
                tertiary = Color.Unspecified
            )
    )

internal val LightForeground =
    SemanticColors<ForegroundColors>(
        default =
            ForegroundColors(
                primary = Color(0xFF1E1E1E),
                onPrimary = Color.Transparent,
                secondary = Color(0xFF757575),
                onSecondary = Color.Unspecified,
                tertiary = Color(0xFFB3B3B3),
                onTertiary = Color.Unspecified
            ),
        neutral =
            ForegroundColors(
                primary = Color(0xFF303030),
                onPrimary = Color(0xFFF3F3F3),
                secondary = Color(0xFF5A5A5A),
                onSecondary = Color(0xFF303030),
                tertiary = Color(0xFF767676),
                onTertiary = Color(0xFF434343)
            ),
        brand =
            ForegroundColors(
                primary = Color(0xFF0562DD),
                onPrimary = Color(0xFFFFFFFF),
                secondary = Color(0xFF418DF2),
                onSecondary = Color(0xFF0562DD),
                tertiary = Color(0xFF569EFB),
                onTertiary = Color(0xFF106FEB)
            ),
        positive =
            ForegroundColors(
                primary = Color(0xFF02542D),
                onPrimary = Color(0xFFEBFFEE),
                secondary = Color(0xFF009951),
                onSecondary = Color(0xFF02542D),
                tertiary = Color(0xFF14AE5C),
                onTertiary = Color(0xFF02542D)
            ),
        warning =
            ForegroundColors(
                primary = Color(0xFF463A16),
                onPrimary = Color(0xFF372E13),
                secondary = Color(0xFF755E1B),
                onSecondary = Color(0xFF5A4A19),
                tertiary = Color(0xFF97771D),
                onTertiary = Color(0xFF463A16)
            ),
        danger =
            ForegroundColors(
                primary = Color(0xFF900B09),
                onPrimary = Color(0xFFFEE9E7),
                secondary = Color(0xFFC00F0C),
                onSecondary = Color(0xFF900B09),
                tertiary = Color(0xFFEC221F),
                onTertiary = Color(0xFF900B09)
            ),
        disabled =
            ForegroundColors(
                primary = Color(0xFFB3B3B3),
                onPrimary = Color(0xFFB3B3B3),
                secondary = Color.Unspecified,
                onSecondary = Color.Unspecified,
                tertiary = Color.Unspecified,
                onTertiary = Color.Unspecified
            )
    )

internal val LightAppColorScheme =
    AppColorScheme(
        background = LightBackground,
        border = LightBorder,
        text = LightForeground,
        icon = LightForeground
    )