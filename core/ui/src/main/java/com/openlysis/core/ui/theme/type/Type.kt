package com.openlysis.core.ui.theme.type

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// From Figma
object Title {
    val Hero =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value10,
            lineHeight = 86.4.sp
        )
    val Level1 =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value08,
            lineHeight = 57.6.sp
        )
    val Level2 =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value07,
            lineHeight = 48.sp
        )
    val Level3 =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value06,
            lineHeight = 38.4.sp
        )
    val Level4 =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value05,
            lineHeight = 28.8.sp
        )
    val Level5 =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = Scales.Value04,
            lineHeight = 24.sp
        )
    val Level6 =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = Scales.Value03,
            lineHeight = 19.2.sp
        )
}

object Body {
    val Large =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value04,
            lineHeight = 28.sp
        )
    val LargeStrong = Large.copy(fontWeight = FontWeight.SemiBold)

    val Base =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value03,
            lineHeight = 22.4.sp
        )
    val BaseStrong = Base.copy(fontWeight = FontWeight.SemiBold)

    val Small =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value02,
            lineHeight = 19.6.sp
        )
    val SmallStrong = Small.copy(fontWeight = FontWeight.SemiBold)

    val ExtraSmall =
        TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value01
        )
    val ExtraSmallStrong = ExtraSmall.copy(fontWeight = FontWeight.SemiBold)
}

// Material Design
val Typography =
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