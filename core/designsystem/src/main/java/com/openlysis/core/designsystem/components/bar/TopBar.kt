package com.openlysis.core.designsystem.components.bar

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.R
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.components.dropdown.AppDropdownMenu
import com.openlysis.core.designsystem.components.dropdown.DropdownMenuItemData
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A top bar.
 *
 * @param onBackClick The action to be performed when the back button of the [TopBar] is clicked.
 * @param state The state object containing the title, menu visibility flag, and menu items for the top bar.
 * @param modifier The modifier to be applied to the top bar for customizing its layout and appearance.
 *                Defaults to [Modifier].
 */
@Composable
fun TopBar(
    onBackClick: () -> Unit,
    state: TopBarState,
    modifier: Modifier = Modifier
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
                onClick = onBackClick,
                displayLabel = false,
                displayIcon = true,
                icon = AppIcons.Back,
                iconAlt = stringResource(R.string.top_bar_back_icon_description)
            )
            Text(
                text = state.title,
                style = LocalAppTypography.current.title5,
                color = LocalAppColorScheme.current.text.brand.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(weight = 1f, fill = true)
            )
            if (state.hasMenu && state.menuItems != null) {
                var isMenuExpanded by remember { mutableStateOf(false) }
                AppDropdownMenu(
                    onExpand = { isMenuExpanded = true },
                    onDismissRequest = { isMenuExpanded = false },
                    expanded = isMenuExpanded,
                    buttonType = ButtonType.Tertiary,
                    dropdownItems = state.menuItems
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
            onBackClick = { },
            state =
                TopBarState(
                    title = "Title preview",
                    hasMenu = true,
                    menuItems =
                        listOf<DropdownMenuItemData>(
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
        )
    }
}