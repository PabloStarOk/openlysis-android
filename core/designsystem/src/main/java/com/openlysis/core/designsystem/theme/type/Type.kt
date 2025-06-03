package com.openlysis.core.designsystem.theme.type

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Default typography of Openlysis.
 */
val LocalAppTypography =
    staticCompositionLocalOf {
        AppTypography(
            titleHero = Title.Hero,
            title1 = Title.Level1,
            title2 = Title.Level2,
            title3 = Title.Level3,
            title4 = Title.Level4,
            title5 = Title.Level5,
            title6 = Title.Level6,
            bodyLarge = Body.Large,
            bodyLargeStrong = Body.LargeStrong,
            bodyBase = Body.Base,
            bodyBaseStrong = Body.BaseStrong,
            bodySmall = Body.Small,
            bodySmallStrong = Body.SmallStrong,
            bodyXSmall = Body.ExtraSmall,
            bodyXSmallStrong = Body.ExtraSmallStrong
        )
    }

/**
 * Contains a material design typography instance as fallback (just in case).
 */
val FallbackTypography =
    Typography(
        displayLarge = Title.Hero,
        headlineLarge = Title.Level1,
        headlineMedium = Title.Level2,
        headlineSmall = Title.Level3,
        titleLarge = Title.Level4,
        titleMedium = Title.Level5,
        titleSmall = Title.Level6,
        bodyLarge = Body.Large,
        bodyMedium = Body.Base,
        bodySmall = Body.Small,
        labelLarge = Body.Large,
        labelMedium = Body.Base,
        labelSmall = Body.Small
    )