package com.openlysis.core.designsystem.theme.type

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.openlysis.core.designsystem.R

/**
 * Defines the Montserrat font family.
 */
@OptIn(ExperimentalTextApi::class)
internal val Montserrat =
    FontFamily(
        Font(
            resId = R.font.montserrat_variable_font_wght,
            weight = FontWeight.Bold,
            variationSettings = FontVariation.Settings(FontVariation.weight(700))
        ),
        Font(
            resId = R.font.montserrat_variable_font_wght,
            weight = FontWeight.SemiBold,
            variationSettings = FontVariation.Settings(FontVariation.weight(600))
        ),
        Font(
            resId = R.font.montserrat_variable_font_wght,
            weight = FontWeight.Normal,
            variationSettings = FontVariation.Settings(FontVariation.weight(400))
        )
    )