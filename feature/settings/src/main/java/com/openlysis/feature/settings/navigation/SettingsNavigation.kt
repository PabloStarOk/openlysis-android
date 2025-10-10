package com.openlysis.feature.settings.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.openlysis.feature.settings.SettingsScreen
import com.openlysis.feature.settings.SettingsScreenViewModel
import kotlinx.serialization.Serializable

/**
 * Serializable object representing the navigation route for the Settings screen.
 */
@Serializable
data object SettingsRoute

/**
 * Navigates to the Settings screen using the provided [NavOptions].
 *
 * @param navOptions Navigation options to customize the navigation behavior.
 */
fun NavController.navigateToSettings(navOptions: NavOptions) =
    this.navigate(SettingsRoute, navOptions = navOptions)

/**
 * Adds the Settings screen composable to the navigation graph.
 *
 * @param enterTransition Lambda for the enter transition animation.
 * @param exitTransition Lambda for the exit transition animation.
 * @param popEnterTransition Lambda for the pop enter transition animation. Defaults to [enterTransition].
 * @param popExitTransition Lambda for the pop exit transition animation. Defaults to [exitTransition].
 */
fun NavGraphBuilder.settingsScreen(
    enterTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.()
    -> @JvmSuppressWildcards EnterTransition?
    ),
    exitTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.()
    -> @JvmSuppressWildcards ExitTransition?
    ),
    popEnterTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.()
    -> @JvmSuppressWildcards EnterTransition?
    ) = enterTransition,
    popExitTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.()
    -> @JvmSuppressWildcards ExitTransition?
    ) = exitTransition
) {
    composable<SettingsRoute>(
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    ) {
        SettingsScreen(
            viewModel = hiltViewModel<SettingsScreenViewModel>()
        )
    }
}