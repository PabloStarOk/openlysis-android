package com.openlysis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

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
}