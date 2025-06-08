package com.openlysis.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.navOptions
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.feature.tools.navigation.navigateToTools
import com.openlysis.ui.AppState

/**
 * App navigation bar with navigation items.
 *
 * @param appState The state of the app, including the NavController and current destination.
 * @param modifier The modifier to be applied to the navigation bar.
 */
@Composable
internal fun AppNavBar(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    val currentDest = appState.currentDestination
    val appColorScheme = LocalAppColorScheme.current.border.default.primary
    val navOptions =
        navOptions {
            launchSingleTop = true
            restoreState = true
        }
    NavigationBar(
        containerColor = LocalAppColorScheme.current.background.default.primary,
        modifier =
            modifier
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = appColorScheme,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                }.padding(
                    horizontal = LocalAppSpacing.current.value300
                )
    ) {
        val isToolsSelected = currentDest?.hasRoute(TopLevelDestination.Tools.route) == true
        AppNavBarItem(
            onClick = { navController.navigateToTools(navOptions) },
            icon = ImageVector.vectorResource(TopLevelDestination.Tools.iconResId),
            iconAlt = stringResource(TopLevelDestination.Tools.iconAltResId),
            label = stringResource(TopLevelDestination.Tools.navBarItemLabelResId),
            selected = isToolsSelected,
            enabled = !isToolsSelected,
            modifier = Modifier.weight(1f)
        )

        val isResultsSelected = currentDest?.hasRoute(TopLevelDestination.Results.route) == true
        AppNavBarItem(
            onClick = { navController.navigate(TemporaryResults) },
            icon = ImageVector.vectorResource(TopLevelDestination.Results.iconResId),
            iconAlt = stringResource(TopLevelDestination.Results.iconAltResId),
            label = stringResource(TopLevelDestination.Results.navBarItemLabelResId),
            selected = isResultsSelected,
            enabled = !isResultsSelected,
            modifier = Modifier.weight(1f)
        )

        val isSettingsSelected = currentDest?.hasRoute(TopLevelDestination.Settings.route) == true
        AppNavBarItem(
            onClick = { navController.navigate(TemporarySettings) },
            icon = ImageVector.vectorResource(TopLevelDestination.Settings.iconResId),
            iconAlt = stringResource(TopLevelDestination.Settings.iconAltResId),
            label = stringResource(TopLevelDestination.Settings.navBarItemLabelResId),
            selected = isSettingsSelected,
            enabled = !isSettingsSelected,
            modifier = Modifier.weight(1f)
        )
    }
}