package com.openlysis.core.designsystem.components.dropdown

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.R
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius

/**
 * Represents a dropdown menu.
 *
 * @param dropdownItems An array of `DropdownMenuItemData` objects, each representing an item in the dropdown menu.
 * @param expanded A boolean indicating whether the dropdown menu is currently visible (expanded) or hidden.
 * @param onExpand A lambda function that is invoked when the user clicks the button to expand the menu.
 * This function should typically update the `expanded` state.
 * @param onDismissRequest A lambda function that is invoked when the user requests to dismiss the menu
 * (e.g., by clicking outside the menu or pressing the escape key).
 * This function should typically update the `expanded` state to `false`.
 * @param modifier Optional [Modifier] to be applied to the root `Column` of the dropdown menu.
 * Defaults to `Modifier`.
 */
@Composable
fun AppDropdownMenu(
    onExpand: () -> Unit,
    onDismissRequest: () -> Unit,
    buttonType: ButtonType,
    expanded: Boolean,
    dropdownItems: Array<DropdownMenuItemData>,
    modifier: Modifier = Modifier,
    buttonSize: SizeType = SizeType.Default
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        AppButton(
            type = buttonType,
            size = buttonSize,
            onClick = onExpand,
            displayLabel = false,
            displayIcon = true,
            icon = ImageVector.vectorResource(R.drawable.menu_icon),
            iconAlt = stringResource(R.string.dropdown_menu_icon_description)
        )

        DropdownMenu(
            modifier =
                Modifier
                    .width(250.dp)
                    .background(LocalAppColorScheme.current.background.default.primary),
            border = BorderStroke(1.dp, LocalAppColorScheme.current.border.default.primary),
            shape = RoundedCornerShape(LocalAppRadius.current.value100),
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            if (dropdownItems.isNotEmpty()) {
                dropdownItems.forEachIndexed { index, item ->
                    AppDropdownMenuItem(item)
                    if (index < dropdownItems.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}