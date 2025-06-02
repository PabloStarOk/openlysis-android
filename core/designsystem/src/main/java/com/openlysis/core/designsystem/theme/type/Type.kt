package com.openlysis.core.designsystem.theme.type

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

/**
 * Defines the set of text styles used throughout the Openlysis application.
 *
 * @property titleHero The largest and most prominent title style, typically used for hero sections or main screen titles.
 * @property title1 A primary title style, smaller than `titleHero`.
 * @property title2 A secondary title style.
 * @property title3 A tertiary title style.
 * @property title4 A smaller title style, often used for subheadings.
 * @property title5 An even smaller title style.
 * @property title6 The smallest title style, suitable for minor headings or labels.
 * @property bodyLarge A larger body text style, for prominent paragraphs or important information.
 * @property bodyLargeStrong A bolded version of `bodyLarge`, for emphasizing larger body text.
 * @property bodyBase The standard body text style, used for most paragraph content.
 * @property bodyBaseStrong A bolded version of `bodyBase`, for emphasizing standard body text.
 * @property bodySmall A smaller body text style, for secondary information or captions.
 * @property bodySmallStrong A bolded version of `bodySmall`, for emphasizing smaller body text.
 * @property bodyXSmall The smallest body text style, often used for fine print or tertiary details.
 * @property bodyXSmallStrong A bolded version of `bodyXSmall`, for emphasizing the smallest body text.
 */
@Immutable
data class OpenlysisTypography(
    val titleHero: TextStyle,
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val title4: TextStyle,
    val title5: TextStyle,
    val title6: TextStyle,
    val bodyLarge: TextStyle,
    val bodyLargeStrong: TextStyle,
    val bodyBase: TextStyle,
    val bodyBaseStrong: TextStyle,
    val bodySmall: TextStyle,
    val bodySmallStrong: TextStyle,
    val bodyXSmall: TextStyle,
    val bodyXSmallStrong: TextStyle
)

/**
 * Default typography of Openlysis.
 */
val LocalTypography =
    staticCompositionLocalOf {
        OpenlysisTypography(
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