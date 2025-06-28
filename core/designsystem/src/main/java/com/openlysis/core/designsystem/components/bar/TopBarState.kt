package com.openlysis.core.designsystem.components.bar

import androidx.compose.runtime.Immutable
import com.openlysis.core.designsystem.components.dropdown.DropdownMenuItemData

/**
 * Represents the state of a top app bar in the UI.
 *
 * @property title The text to be displayed as the top bar title.
 * @property hasMenu Indicates whether the top bar should display a menu. Defaults to false.
 * @property menuItems Optional list of dropdown menu items to be displayed when the menu is active.
 */
@Immutable
data class TopBarState(
    val title: String,
    val hasMenu: Boolean = false,
    val menuItems: List<DropdownMenuItemData>? = null
)