package com.openlysis.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openlysis.feature.tools.navigation.ToolsBaseRoute
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
                navController.navigate(TemporaryResults)
                // TODO: Implement navigation to display and update results of this new analysis.
            },
            enterTransition = {
                val toNestedGraph =
                    this.initialState.destination.parent?.hierarchy?.any {
                        it.hasRoute(
                            route = ToolsBaseRoute::class
                        )
                    } == true
                if (toNestedGraph) {
                    null
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec =
                            tween(
                                durationMillis = 300,
                                easing = EaseInOut
                            )
                    )
                }
            },
            exitTransition = {
                val toNestedGraph =
                    this.targetState.destination.parent?.hierarchy?.any {
                        it.hasRoute(
                            route = ToolsBaseRoute::class
                        )
                    } == true
                if (toNestedGraph) {
                    null
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec =
                            tween(
                                durationMillis = 300,
                                easing = EaseInOut
                            )
                    )
                }
            }
        )

        composable<TemporaryResults> {
            Text(text = "Results screen.")
        }

        composable<TemporarySettings> {
            Text(text = "Settings screen.")
        }
    }
}