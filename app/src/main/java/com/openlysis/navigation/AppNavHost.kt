package com.openlysis.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openlysis.feature.results.navigation.ResultsBaseRoute
import com.openlysis.feature.results.navigation.navigateToEmailAnalysisPreviews
import com.openlysis.feature.results.navigation.navigateToMessageAnalysisDetails
import com.openlysis.feature.results.navigation.navigateToSmsAnalysisPreviews
import com.openlysis.feature.results.navigation.resultsScreen
import com.openlysis.feature.tools.navigation.ToolsBaseRoute
import com.openlysis.feature.tools.navigation.ToolsRoute
import com.openlysis.feature.tools.navigation.toolsScreen
import com.openlysis.ui.AppState

/**
 * The navigation graph for the application.
 *
 * @param appState The [AppState] instance that holds the navigation controller and other
 *                 application-wide state.
 * @param modifier Optional [Modifier] to be applied to the NavHost.
 */
@Composable
internal fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = ToolsBaseRoute,
        modifier = modifier
    ) {
        toolsScreen(
            navController = navController,
            onTopBarUpdate = appState::updateTopBarState,
            onMessageAnalysisStart = {
                navController.navigateToMessageAnalysisDetails(it.id, it.message.type)
            },
            onFileAnalysisStart = {
                navController.navigate(ResultsBaseRoute)
                // TODO: Implement navigation to display and update results of this new analysis.
            },
            onUrlAnalysisStart = {
                navController.navigate(ResultsBaseRoute)
                // TODO: Implement navigation to display and update results of this new analysis.
            },
            enterTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.initialState.destination)
                if (isTopLevelDest) {
                    slideIntoContainer(
                        towards = SlideDirection.Right,
                        animationSpec =
                            tween(
                                durationMillis = 300,
                                easing = EaseInOut
                            )
                    )
                } else {
                    slideIntoContainer(SlideDirection.Up) + fadeIn()
                }
            },
            exitTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.targetState.destination)
                if (isTopLevelDest) {
                    slideOutOfContainer(
                        towards = SlideDirection.Left,
                        animationSpec =
                            tween(
                                durationMillis = 300,
                                easing = EaseInOut
                            )
                    )
                } else {
                    slideOutOfContainer(SlideDirection.Down) + fadeOut()
                }
            }
        )

        resultsScreen(
            onTopBarUpdate = appState::updateTopBarState,
            onEmailResultsClick = navController::navigateToEmailAnalysisPreviews,
            onSmsResultsClick = navController::navigateToSmsAnalysisPreviews,
            onMessagePreviewDetailsClick = { id, messageType ->
                navController.navigateToMessageAnalysisDetails(id, messageType)
            },
            enterTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.initialState.destination)
                if (isTopLevelDest) {
                    val toTools = this.initialState.destination.hasRoute(ToolsRoute::class)
                    val direction =
                        if (toTools) {
                            SlideDirection.Left
                        } else {
                            SlideDirection.Right
                        }
                    slideIntoContainer(
                        towards = direction,
                        animationSpec =
                            tween(
                                durationMillis = 300,
                                easing = EaseInOut
                            )
                    )
                } else {
                    slideIntoContainer(SlideDirection.Up) + fadeIn()
                }
            },
            exitTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.targetState.destination)
                if (isTopLevelDest) {
                    val toTools = this.targetState.destination.hasRoute(ToolsRoute::class)
                    val direction =
                        if (toTools) {
                            SlideDirection.Right
                        } else {
                            SlideDirection.Left
                        }
                    slideOutOfContainer(
                        towards = direction,
                        animationSpec =
                            tween(
                                durationMillis = 300,
                                easing = EaseInOut
                            )
                    )
                } else {
                    slideOutOfContainer(SlideDirection.Down) + fadeOut()
                }
            }
        )

        composable<TemporarySettings> {
            Text(text = "Settings screen.")
        }
    }
}