package com.openlysis.core.designsystem.theme.type

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Defines various text styles for titles within the application.
 */
internal object Title {
    val Hero =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value10,
            lineHeight = 86.4.sp
        )
    val Level1 =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value08,
            lineHeight = 57.6.sp
        )
    val Level2 =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value07,
            lineHeight = 48.sp
        )
    val Level3 =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value06,
            lineHeight = 38.4.sp
        )
    val Level4 =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = Scales.Value05,
            lineHeight = 28.8.sp
        )
    val Level5 =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = Scales.Value04,
            lineHeight = 24.sp
        )
    val Level6 =
        TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = Scales.Value03,
            lineHeight = 19.2.sp
        )
}