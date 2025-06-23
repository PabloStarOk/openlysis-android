package com.openlysis.feature.tools.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.openlysis.feature.tools.ToolsScreen
import com.openlysis.feature.tools.data.ToolsDataSource
import kotlinx.serialization.Serializable

/**
 * Route for accessing the tools screen.
 */
@Serializable
object ToolsRoute

/**
 * Provides functionality to navigate to the tools screen.
 */
fun NavController.navigateToTools(navOptions: NavOptions) =
    navigate(ToolsRoute, navOptions = navOptions)

/**
 * Adds the tool screen to the navigation graph.
 */
fun NavGraphBuilder.toolsScreen(
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
    composable<ToolsRoute>(
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    ) {
        ToolsScreen(ToolsDataSource())
    }
}