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

@Serializable
object ToolsNestedGraphRoute

@Serializable
object ToolsRoute

fun NavController.navigateToTools(navOptions: NavOptions) =
    navigate(ToolsRoute, navOptions = navOptions)

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