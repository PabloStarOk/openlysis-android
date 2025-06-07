package com.openlysis.feature.tools.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.openlysis.feature.tools.ToolsScreen
import com.openlysis.feature.tools.data.ToolsDataSource
import kotlinx.serialization.Serializable

/**
 * Route to the tools nested graph.
 */
@Serializable
object ToolsNestedGraphRoute

/**
 * Route for accessing the tools main screen of the nested graph.
 */
@Serializable
object ToolsRoute

/**
 * Provides functionality to navigate to the tools screen.
 */
fun NavController.navigateToTools(navOptions: NavOptions) =
    navigate(ToolsRoute, navOptions = navOptions)

/**
 * Adds the tool screen as a nested graph to the navigation.
 */
fun NavGraphBuilder.toolsScreen(
    enterTransition: EnterTransition,
    exitTransition: ExitTransition,
    popEnterTransition: EnterTransition = enterTransition,
    popExitTransition: ExitTransition = exitTransition
) {
    navigation<ToolsNestedGraphRoute> (
        startDestination = ToolsRoute
    ) {
        composable<ToolsRoute>(
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) {
            ToolsScreen(ToolsDataSource())
        }
    }
}