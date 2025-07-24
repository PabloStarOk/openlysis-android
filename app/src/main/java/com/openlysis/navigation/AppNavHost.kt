package com.openlysis.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.auth.model.AuthenticationType
import com.openlysis.feature.auth.navigation.authScreen
import com.openlysis.feature.auth.navigation.navigateToAuthentication
import com.openlysis.feature.results.navigation.ResultType
import com.openlysis.feature.results.navigation.navigateToFileMultiAnalysisDetails
import com.openlysis.feature.results.navigation.navigateToMessageAnalysisDetails
import com.openlysis.feature.results.navigation.navigateToPreviews
import com.openlysis.feature.results.navigation.navigateToUrlMultiAnalysisDetails
import com.openlysis.feature.results.navigation.resultsScreen
import com.openlysis.feature.tools.navigation.ToolsRoute
import com.openlysis.feature.tools.navigation.navigateToTools
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
        startDestination = appState.startDestinationRoute,
        modifier = modifier
    ) {
        authScreen(
            onSignUpRequest = { navController.navigateToAuthentication(AuthenticationType.SignUp) },
            onSignInRequest = { navController.navigateToAuthentication(AuthenticationType.SignIn) },
            onAuthenticated = {
                val navOptions =
                    navOptions {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                            saveState = false
                        }

                        launchSingleTop = true
                        restoreState = false
                    }
                navController.navigateToTools(navOptions)
            }
        )

        toolsScreen(
            navController = navController,
            onTopBarUpdate = appState::updateTopBarState,
            onMessageAnalysisStart = {
                navController.navigateToMessageAnalysisDetails(it.id, it.message.type)
            },
            onFileAnalysisStart = { navController.navigateToFileMultiAnalysisDetails(it.id) },
            onUrlAnalysisStart = { navController.navigateToUrlMultiAnalysisDetails(it.id) },
            enterTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.initialState.destination)
                if (isTopLevelDest) {
                    fromTopDestinationEnterTransition(SlideDirection.Right)
                } else {
                    slideIntoContainer(SlideDirection.Up) + fadeIn()
                }
            },
            exitTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.targetState.destination)
                if (isTopLevelDest) {
                    toTopDestinationExitTransition(SlideDirection.Left)
                } else {
                    slideOutOfContainer(SlideDirection.Down) + fadeOut()
                }
            }
        )

        resultsScreen(
            onTopBarUpdate = appState::updateTopBarState,
            onResultsCardClick = navController::navigateToPreviews,
            onPreviewDetailsClick = { id, previewType ->
                when (previewType) {
                    ResultType.Email ->
                        navController.navigateToMessageAnalysisDetails(id, MessageType.Email)
                    ResultType.Sms ->
                        navController.navigateToMessageAnalysisDetails(id, MessageType.Sms)
                    ResultType.File -> navController.navigateToFileMultiAnalysisDetails(id)
                    ResultType.Url -> navController.navigateToUrlMultiAnalysisDetails(id)
                }
            },
            enterTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.initialState.destination)
                if (isTopLevelDest) {
                    val toTools = this.initialState.destination.hasRoute(ToolsRoute::class)
                    fromTopDestinationEnterTransition(
                        direction =
                            if (toTools) {
                                SlideDirection.Left
                            } else {
                                SlideDirection.Right
                            }
                    )
                } else {
                    slideIntoContainer(SlideDirection.Up) + fadeIn()
                }
            },
            exitTransition = {
                val isTopLevelDest = appState.isTopLevelDestination(this.targetState.destination)
                if (isTopLevelDest) {
                    val toTools = this.targetState.destination.hasRoute(ToolsRoute::class)
                    toTopDestinationExitTransition(
                        direction =
                            if (toTools) {
                                SlideDirection.Right
                            } else {
                                SlideDirection.Left
                            }
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

private fun AnimatedContentTransitionScope<NavBackStackEntry>.fromTopDestinationEnterTransition(
    direction: SlideDirection
): EnterTransition =
    slideIntoContainer(
        towards = direction,
        animationSpec =
            tween(
                durationMillis = 300,
                easing = EaseInOut
            )
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.toTopDestinationExitTransition(
    direction: SlideDirection
): ExitTransition =
    slideOutOfContainer(
        towards = direction,
        animationSpec =
            tween(
                durationMillis = 300,
                easing = EaseInOut
            )
    )