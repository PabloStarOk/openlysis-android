package com.openlysis.feature.tools.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a tool item displayed in the user interface, typically within a bottom navigation bar
 * or a similar list of interactive elements.
 *
 * Each tool is characterized by its display properties (name, description, icon) and the action
 * it performs when clicked.
 *
 * @property nameResource A string resource ID for the name of the tool. This is displayed to the user.
 * @property descriptionResource A string resource ID for a brief description of the tool.
 *                                This can be used for accessibility or tooltips.
 * @property iconResource The [ImageVector] representing the visual icon of the tool.
 * @property iconAltResource A string resource ID for the accessibility description of the icon.
 *                             This is used by screen readers to describe the icon.
 * @property onClick A lambda function that will be executed when the tool is clicked.
 */
internal class Tool(
    @StringRes val nameResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val iconResource: Int,
    @StringRes val iconAltResource: Int,
    val onClick: () -> Unit
)