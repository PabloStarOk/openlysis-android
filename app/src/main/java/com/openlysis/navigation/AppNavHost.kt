package com.openlysis.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    NavHost(
        navController = appState.navController,
        startDestination = ToolsRoute,
        modifier = modifier
    ) {
        toolsScreen(
            onMessageAnalysisStart = {
                appState.navController.navigate(TemporaryResults)
                // TODO: Implement navigation to display and update results of this new analysis.
            },
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = EaseInOut
                        )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec =
                        tween(
                            durationMillis = 300,
                            easing = EaseInOut
                        )
                )
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