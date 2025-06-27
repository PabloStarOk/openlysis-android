package com.openlysis.feature.tools.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.tools.ToolsScreen
import com.openlysis.feature.tools.ToolsScreenViewModel
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
 * Adds the tools screen to the navigation graph with specified transitions.
 *
 * @param onMessageAnalysisStart Callback triggered when message analysis is started
 * @param enterTransition Animation played when the screen enters
 * @param exitTransition Animation played when the screen exits
 * @param popEnterTransition Animation played when the screen re-enters after pop (defaults to enterTransition)
 * @param popExitTransition Animation played when the screen is popped (defaults to exitTransition)
 */
fun NavGraphBuilder.toolsScreen(
    onMessageAnalysisStart: (MessageAnalysis) -> Unit,
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
        ToolsScreen(
            onMessageAnalysisStart = onMessageAnalysisStart,
            viewModel = hiltViewModel<ToolsScreenViewModel>()
        )
    }
}