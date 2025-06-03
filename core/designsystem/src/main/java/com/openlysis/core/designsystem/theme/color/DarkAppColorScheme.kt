package com.openlysis.core.designsystem.theme.color

import androidx.compose.ui.graphics.Color

internal val DarkBackground =
    SemanticColors<SurfaceColors>(
        default =
            SurfaceColors(
                primary = Color(0xFF1E1E1E),
                primaryActive = Color(0xFF383838),
                secondary = Color(0xFF2C2C2C),
                secondaryActive = Color(0xFF1E1E1E),
                tertiary = Color(0xFF444444),
                tertiaryActive = Color(0xFF383838)
            ),
        neutral =
            SurfaceColors(
                primary = Color(0xFFB2B2B2),
                primaryActive = Color(0xFF949494),
                secondary = Color(0xFF5A5A5A),
                secondaryActive = Color(0xFF434343),
                tertiary = Color(0xFF303030),
                tertiaryActive = Color(0xFF242424)
            ),
        brand =
            SurfaceColors(
                primary = Color(0xFF0F458D),
                primaryActive = Color(0xFF0B4FAB),
                secondary = Color(0xFF163359),
                secondaryActive = Color(0xFF0F458D),
                tertiary = Color(0xFF15253B),
                tertiaryActive = Color(0xFF163359)
            ),
        positive =
            SurfaceColors(
                primary = Color(0xFF02542D),
                primaryActive = Color(0xFF008043),
                secondary = Color(0xFF024023),
                secondaryActive = Color(0xFF02542D),
                tertiary = Color(0xFF062D1B),
                tertiaryActive = Color(0xFF024023)
            ),
        warning =
            SurfaceColors(
                primary = Color(0xFF755E1B),
                primaryActive = Color(0xFF97771D),
                secondary = Color(0xFF5A4A19),
                secondaryActive = Color(0xFF755E1B),
                tertiary = Color(0xFF372E13),
                tertiaryActive = Color(0xFF463A16)
            ),
        danger =
            SurfaceColors(
                primary = Color(0xFF900B09),
                primaryActive = Color(0xFFC00F0C),
                secondary = Color(0xFF4D0B0A),
                secondaryActive = Color(0xFF690807),
                tertiary = Color(0xFF300603),
                tertiaryActive = Color(0xFF4D0B0A)
            ),
        disabled =
            SurfaceColors(
                primary = Color(0xFF383838),
                primaryActive = Color.Unspecified,
                secondary = Color.Unspecified,
                secondaryActive = Color.Unspecified,
                tertiary = Color.Unspecified,
                tertiaryActive = Color.Unspecified
            )
    )

internal val DarkBorder =
    SemanticColors<PaletteColors>(
        default =
            PaletteColors(
                primary = Color(0xFF444444),
                secondary = Color(0xFF757575),
                tertiary = Color(0xFFB3B3B3)
            ),
        neutral =
            PaletteColors(
                primary = Color(0xFFF3F3F3),
                secondary = Color(0xFF949494),
                tertiary = Color(0xFF767676)
            ),
        brand =
            PaletteColors(
                primary = Color(0xFFBFDCFF),
                secondary = Color(0xFF94C5FF),
                tertiary = Color(0xFF69AFFF)
            ),
        positive =
            PaletteColors(
                primary = Color(0xFFCFF7D3),
                secondary = Color(0xFF85E0A3),
                tertiary = Color(0xFF009951)
            ),
        warning =
            PaletteColors(
                primary = Color(0xFFFFF1C2),
                secondary = Color(0xFFE8B931),
                tertiary = Color(0xFF97771D)
            ),
        danger =
            PaletteColors(
                primary = Color(0xFFFDD3D0),
                secondary = Color(0xFFF4776A),
                tertiary = Color(0xFFEC221F)
            ),
        disabled =
            PaletteColors(
                primary = Color(0xFF444444),
                secondary = Color.Unspecified,
                tertiary = Color.Unspecified
            )
    )

internal val DarkForeground =
    SemanticColors<ForegroundColors>(
        default =
            ForegroundColors(
                primary = Color(0xFFFFFFFF),
                onPrimary = Color(0xB3FFFFFF),
                secondary = Color(0x66FFFFFF),
                onSecondary = Color.Unspecified,
                tertiary = Color.Unspecified,
                onTertiary = Color.Unspecified
            ),
        neutral =
            ForegroundColors(
                primary = Color(0xFFE3E3E3),
                onPrimary = Color(0xFF242424),
                secondary = Color(0xFFCDCDCD),
                onSecondary = Color(0xFFF3F3F3),
                tertiary = Color(0xFFB2B2B2),
                onTertiary = Color(0xFFF3F3F3)
            ),
        brand =
            ForegroundColors(
                primary = Color(0xFFBFDCFF),
                onPrimary = Color(0xFFEEF6FF),
                secondary = Color(0xFF94C5FF),
                onSecondary = Color(0xFF94C5FF),
                tertiary = Color(0xFF69AFFF),
                onTertiary = Color(0xFF69AFFF)
            ),
        positive =
            ForegroundColors(
                primary = Color(0xFFCFF7D3),
                onPrimary = Color(0xFFEBFFEE),
                secondary = Color(0xFF85E0A3),
                onSecondary = Color(0xFFEBFFEE),
                tertiary = Color(0xFF009951),
                onTertiary = Color(0xFFEBFFEE)
            ),
        warning =
            ForegroundColors(
                primary = Color(0xFFFFF1C2),
                onPrimary = Color(0xFF372E13),
                secondary = Color(0xFFE8B931),
                onSecondary = Color(0xFFFFFBEB),
                tertiary = Color(0xFF97771D),
                onTertiary = Color(0xFFFFFBEB)
            ),
        danger =
            ForegroundColors(
                primary = Color(0xFFFDD3D0),
                onPrimary = Color(0xFFFEE9E7),
                secondary = Color(0xFFF4776A),
                onSecondary = Color(0xFFFEE9E7),
                tertiary = Color(0xFFEC221F),
                onTertiary = Color(0xFFFEE9E7)
            ),
        disabled =
            ForegroundColors(
                primary = Color(0xFF757575),
                onPrimary = Color(0xFFB3B3B3),
                secondary = Color.Unspecified,
                onSecondary = Color.Unspecified,
                tertiary = Color.Unspecified,
                onTertiary = Color.Unspecified
            )
    )

internal val DarkAppColorScheme =
    AppColorScheme(
        background = DarkBackground,
        border = DarkBorder,
        text = DarkForeground,
        icon = DarkForeground
    )