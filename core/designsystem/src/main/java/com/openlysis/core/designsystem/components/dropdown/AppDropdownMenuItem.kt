package com.openlysis.core.designsystem.components.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * Represents an item for [AppDropdownMenu].
 *
 * @param menuItemData The data for the dropdown menu item, containing the icon, icon alternative text, label, and onClick action.
 * @param modifier The [Modifier] to be applied to the menu item. Defaults to [Modifier].
 */
@Composable
internal fun AppDropdownMenuItem(
    menuItemData: DropdownMenuItemData,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple =
        ripple(
            bounded = true,
            color = LocalAppColorScheme.current.background.brand.primary
        )

    Box(
        modifier =
            modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple,
                    enabled = true,
                    role = Role.DropdownList,
                    onClick = menuItemData.onClick
                ).fillMaxWidth()
                .fillMaxHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                modifier
                    .padding(
                        vertical = LocalAppSpacing.current.value200,
                        horizontal = LocalAppSpacing.current.value400
                    )
        ) {
            Icon(
                menuItemData.icon,
                contentDescription = menuItemData.iconAlt,
                tint = LocalAppColorScheme.current.icon.default.primary
            )

            Text(
                text = menuItemData.label,
                style = LocalAppTypography.current.bodyBase,
                color = LocalAppColorScheme.current.text.default.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}