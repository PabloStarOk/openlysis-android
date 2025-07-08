package com.openlysis.feature.results.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.feature.results.EmailPreviewsScreenViewModel
import com.openlysis.feature.results.PreviewsScreen
import com.openlysis.feature.results.R
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
 * Route for accessing the email analysis previews screen.
 */
@Serializable
data object EmailAnalysisPreviewsRoute

/**
 * Provides functionality to navigate to the analysis results screen.
 */
fun NavController.navigateToResults(navOptions: NavOptions) =
    this.navigate(ResultsBaseRoute, navOptions = navOptions)

/**
 * Provides functionality to navigate to the email analysis previews screen.
 */
fun NavController.navigateToEmailAnalysisPreviews() = this.navigate(EmailAnalysisPreviewsRoute)

/**
 * Adds the analysis results screens to the navigation graph.
 *
 * @param onTopBarUpdate Callback to update top bar for screens.
 * @param onEmailResultsClick Callback to invoke when the email analysis results card is clicked.
 * @param enterTransition Animation played when the screen enters
 * @param exitTransition Animation played when the screen exits
 * @param popEnterTransition Animation played when the screen re-enters after pop (defaults to enterTransition)
 * @param popExitTransition Animation played when the screen is popped (defaults to exitTransition)
 */
fun NavGraphBuilder.resultsScreen(
    onTopBarUpdate: (TopBarState) -> Unit,
    onEmailResultsClick: () -> Unit,
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
                onEmailResultsClick = onEmailResultsClick,
                onSmsResultsClick = { },
                onFileResultsClick = { },
                onUrlResultsClick = { }
            )
        }

        composable<EmailAnalysisPreviewsRoute>(
            enterTransition = {
                slideIntoContainer(towards = SlideDirection.Down) +
                    fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = SlideDirection.Up
                ) + fadeOut()
            }
        ) {
            PreviewsScreen(
                viewModel = hiltViewModel<EmailPreviewsScreenViewModel>(),
                onTopBarUpdate = onTopBarUpdate,
                screenTitle = stringResource(R.string.email_previews_screen_title),
                previewCardHeaderLabel = stringResource(R.string.email_previews_cards_header_label)
            )
        }
    }
}