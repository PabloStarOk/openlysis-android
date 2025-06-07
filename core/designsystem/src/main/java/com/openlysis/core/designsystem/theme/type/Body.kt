package com.openlysis.core.designsystem.theme.type

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Defines various text styles for the body within the application.
 */
internal object Body {
    val Large =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value04,
            lineHeight = 28.sp
        )
    val LargeStrong = Large.copy(fontWeight = FontWeight.SemiBold)

    val Base =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value03,
            lineHeight = 22.4.sp
        )
    val BaseStrong = Base.copy(fontWeight = FontWeight.SemiBold)

    val Small =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value02,
            lineHeight = 19.6.sp
        )
    val SmallStrong = Small.copy(fontWeight = FontWeight.SemiBold)

    val ExtraSmall =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = Scales.Value01
        )
    val ExtraSmallStrong = ExtraSmall.copy(fontWeight = FontWeight.SemiBold)
}