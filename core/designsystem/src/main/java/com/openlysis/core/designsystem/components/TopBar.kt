package com.openlysis.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openlysis.core.designsystem.R
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.components.dropdown.AppDropdownMenu
import com.openlysis.core.designsystem.components.dropdown.DropdownMenuItemData
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * Represents a top bar for a single screen.
 *
 * @param title The title to be displayed in the top bar.
 * @param navController The navigation controller for handling back navigation.
 * @param modifier The modifier to be applied to the top bar.
 * @param onBackButtonClick The action to be performed when the back button is clicked. Defaults to popping the back stack.
 * @param hasDropdownMenu Whether to display the dropdown menu. Defaults to false.
 * @param dropdownItems An array of [DropdownMenuItemData] to be displayed in the dropdown menu. Defaults to an empty array.
 */
@Composable
fun TopBar(
    title: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit = { navController.popBackStack() },
    hasDropdownMenu: Boolean = false,
    dropdownItems: Array<DropdownMenuItemData> = arrayOf<DropdownMenuItemData>()
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(LocalAppColorScheme.current.background.default.primary)
                .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = LocalAppSpacing.current.value200,
                        horizontal = LocalAppSpacing.current.value300
                    ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300)
        ) {
            AppButton(
                type = ButtonType.Tertiary,
                size = SizeType.Default,
                onClick = onBackButtonClick,
                displayLabel = false,
                displayIcon = true,
                icon = ImageVector.vectorResource(R.drawable.arrow_left_icon),
                iconAlt = stringResource(R.string.top_bar_back_icon_description)
            )
            Text(
                text = title,
                style = LocalAppTypography.current.title5,
                color = LocalAppColorScheme.current.text.brand.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(weight = 1f, fill = true)
            )
            if (hasDropdownMenu) {
                var isMenuExpanded by remember { mutableStateOf(false) }
                AppDropdownMenu(
                    onExpand = { isMenuExpanded = true },
                    onDismissRequest = { isMenuExpanded = false },
                    expanded = isMenuExpanded,
                    buttonType = ButtonType.Tertiary,
                    dropdownItems = dropdownItems
                )
            }
        }
        HorizontalDivider(
            color = LocalAppColorScheme.current.border.default.primary
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    heightDp = 200,
    device = "id:small_phone"
)
@Composable
private fun TopBarPreview() {
    OpenlysisTheme(darkTheme = false) {
        TopBar(
            title = "Title preview",
            navController = rememberNavController(),
            hasDropdownMenu = true,
            dropdownItems =
                arrayOf<DropdownMenuItemData>(
                    DropdownMenuItemData(
                        onClick = { },
                        label = "Do something",
                        icon = Icons.Outlined.Settings,
                        iconAlt = "Settings icon."
                    ),
                    DropdownMenuItemData(
                        onClick = { },
                        label = "Do something different",
                        icon = Icons.Outlined.Build,
                        iconAlt = "Settings icon."
                    )
                )
        )
    }
}