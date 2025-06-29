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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.ui.AppState
import kotlin.reflect.KClass

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
    val currentDest = appState.currentDestination
    val appColorScheme = LocalAppColorScheme.current.border.default.primary
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
        appState.topLevelDestinations.forEach { destination ->
            val isSelected = currentDest.isRouteInHierarchy(destination.baseRoute)
            AppNavBarItem(
                onClick = { appState.navigateToTopLevelDestination(destination) },
                icon = ImageVector.vectorResource(destination.iconResId),
                iconAlt = stringResource(destination.iconAltResId),
                label = stringResource(destination.navBarItemLabelResId),
                selected = isSelected,
                enabled = !isSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } == true