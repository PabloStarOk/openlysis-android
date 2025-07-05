package com.openlysis.feature.results.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.openlysis.feature.results.ResultsScreen
import kotlinx.serialization.Serializable

/**
 * Base navigation route for the analysis results navigation graph.
 */
@Serializable
data object ResultsBaseRoute

/**
 * Route for accessing the analysis results screen, which provides functionality
 * to access other screens to review analysis results.
 */
@Serializable
data object ResultsRoute

/**
 * Provides functionality to navigate to the analysis results screen.
 */
fun NavController.navigateToResults(navOptions: NavOptions) =
    this.navigate(ResultsBaseRoute, navOptions = navOptions)

/**
 * Adds the analysis results screens to the navigation graph.
 *
 * @param enterTransition Animation played when the screen enters
 * @param exitTransition Animation played when the screen exits
 * @param popEnterTransition Animation played when the screen re-enters after pop (defaults to enterTransition)
 * @param popExitTransition Animation played when the screen is popped (defaults to exitTransition)
 */
fun NavGraphBuilder.resultsScreen(
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
    navigation<ResultsBaseRoute>(startDestination = ResultsRoute) {
        composable<ResultsRoute>(
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition
        ) {
            ResultsScreen(
                viewModel = hiltViewModel(),
                onEmailResultsClick = { },
                onSmsResultsClick = { },
                onFileResultsClick = { },
                onUrlResultsClick = { }
            )
        }
    }
}