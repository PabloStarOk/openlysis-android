package com.openlysis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.feature.results.navigation.navigateToResults
import com.openlysis.feature.tools.navigation.navigateToTools
import com.openlysis.navigation.TemporarySettings
import com.openlysis.navigation.TopLevelDestination

/**
 * Creates and remembers an instance of [AppState].
 *
 * @return An instance of [AppState] that is remembered across recompositions.
 */
@Composable
internal fun rememberAppState(): AppState {
    val navController = rememberNavController()
    return remember {
        AppState(navController)
    }
}

/**
 * The overall state of the application.
 *
 * @property navController The [NavHostController] used for navigating between screens.
 */
@Stable
internal class AppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry =
                navController.currentBackStackEntryAsState()
            return currentEntry.value?.destination
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() =
            TopLevelDestination.entries.firstOrNull {
                currentDestination?.hasRoute(it.route) == true
            }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    private val topBarMutableState =
        mutableStateOf(
            TopBarState(
                title = "",
                hasMenu = false
            )
        )

    val topBarState: State<TopBarState> = topBarMutableState

    /**
     * Updates the state of the top bar with the provided new state.
     *
     * @param newState The new [TopBarState] to be applied to the top bar.
     */
    fun updateTopBarState(newState: TopBarState) {
        topBarMutableState.value = newState
    }

    /**
     * Navigates to a top-level destination in the app.
     *
     * @param destination The [TopLevelDestination] to navigate to, which can be Tools, Results, or Settings.
     */
    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        val navOptions =
            navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        when (destination) {
            TopLevelDestination.Tools -> navController.navigateToTools(navOptions)
            TopLevelDestination.Results -> navController.navigateToResults(navOptions)
            TopLevelDestination.Settings -> navController.navigate(TemporarySettings, navOptions)
        }
    }

    /**
     * Checks if the given destination is a top-level destination in the app's navigation hierarchy.
     *
     * @param destination The [NavDestination] to check.
     * @return `true` if the destination is a top-level destination, `false` otherwise.
     */
    fun isTopLevelDestination(destination: NavDestination): Boolean =
        topLevelDestinations.any {
            destination.hasRoute(it.route)
        }
}