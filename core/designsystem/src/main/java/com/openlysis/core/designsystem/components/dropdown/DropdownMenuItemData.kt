package com.openlysis.core.designsystem.components.dropdown

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the data required to display a single item within a dropdown menu.
 *
 * @property label The text to be displayed for this menu item. This is the primary visual identifier.
 * @property icon The [ImageVector] to be displayed as an icon next to the label.
 * @property iconAlt A textual description of the icon to improve accessibility.
 * @property onClick A lambda function that will be executed when this menu item is clicked or selected.
 */
@Immutable
data class DropdownMenuItemData(
    val onClick: () -> Unit,
    val label: String,
    val icon: ImageVector,
    val iconAlt: String
)