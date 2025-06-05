package com.openlysis.core.designsystem.components.button

import androidx.compose.ui.graphics.Color

/**
 * A map that associates each [ButtonType] with its corresponding [ButtonTypeColors].
 */
internal val ButtonTypeColorsMap =
    mapOf<ButtonType, ButtonTypeColors>(
        Pair(
            ButtonType.Primary,
            ButtonTypeColors(
                getBackgroundColor = { scheme -> scheme.background.brand.primary },
                getBorderColor = { scheme -> scheme.border.brand.primary },
                getForegroundColor = { scheme -> scheme.text.brand.onPrimary },
                getRippleColor = { scheme -> scheme.background.brand.primaryActive }
            )
        ),
        Pair(
            ButtonType.Secondary,
            ButtonTypeColors(
                getBackgroundColor = { Color.Transparent },
                getBorderColor = { scheme -> scheme.border.brand.primary },
                getForegroundColor = { scheme -> scheme.text.brand.primary },
                getRippleColor = { scheme -> scheme.background.brand.secondaryActive }
            )
        ),
        Pair(
            ButtonType.Tertiary,
            ButtonTypeColors(
                getBackgroundColor = { Color.Transparent },
                getBorderColor = { Color.Transparent },
                getForegroundColor = { scheme -> scheme.text.brand.primary },
                getRippleColor = { scheme -> scheme.background.brand.tertiaryActive }
            )
        ),
        Pair(
            ButtonType.Positive,
            ButtonTypeColors(
                getBackgroundColor = { Color.Transparent },
                getBorderColor = { scheme -> scheme.border.positive.primary },
                getForegroundColor = { scheme -> scheme.text.positive.primary },
                getRippleColor = { scheme -> scheme.background.positive.tertiaryActive }
            )
        ),
        Pair(
            ButtonType.Danger,
            ButtonTypeColors(
                getBackgroundColor = { Color.Transparent },
                getBorderColor = { scheme -> scheme.border.danger.primary },
                getForegroundColor = { scheme -> scheme.text.danger.primary },
                getRippleColor = { scheme -> scheme.background.danger.tertiaryActive }
            )
        )
    )