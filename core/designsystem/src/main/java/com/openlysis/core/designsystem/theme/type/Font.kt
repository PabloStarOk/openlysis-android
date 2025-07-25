package com.openlysis.core.designsystem.theme.type

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.openlysis.core.designsystem.R

/**
 * Defines the Montserrat font family.
 */
internal val Montserrat =
    FontFamily(
        Font(
            resId = R.font.montserrat_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.montserrat_semibold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resId = R.font.montserrat,
            weight = FontWeight.Normal
        )
    )