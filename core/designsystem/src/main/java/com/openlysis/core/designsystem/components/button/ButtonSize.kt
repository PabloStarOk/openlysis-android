package com.openlysis.core.designsystem.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.openlysis.core.designsystem.theme.size.Spacing
import com.openlysis.core.designsystem.theme.type.AppTypography

/**
 * Represents the different visual configurations for a button's size.
 *
 * @property getPadding A lambda function that takes the application's [Spacing]
 *   configuration and returns the [PaddingValues] to be applied to the button.
 *   This allows the padding to adapt to the overall spacing theme.
 * @property getGap A lambda function that takes the application's [Spacing]
 *   configuration and returns the [Dp] value for the space between the icon
 *   (if present) and the button's text. This allows the gap to adapt to the
 *   overall spacing theme.
 * @property getTextStyle A lambda function that takes the application's [AppTypography]
 *   configuration and returns the [TextStyle] to be applied to the button's text.
 *   This allows the text style to adapt to the overall typography theme.
 * @property height The height of the button, specified in [Dp]. This ensures
 *   buttons of this size do not exceed a certain vertical dimension.
 * @property iconSize The size of the icon (if present) within the button, specified in [Dp].
 */
@Immutable
data class ButtonSize(
    val getPadding: (Spacing) -> PaddingValues,
    val getGap: (Spacing) -> Dp,
    val getTextStyle: (AppTypography) -> TextStyle,
    val height: Dp,
    val iconSize: Dp
)