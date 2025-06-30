package com.openlysis.feature.tools.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.analysis.FileMultiAnalysis
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.feature.tools.EmailAnalysisToolScreen
import com.openlysis.feature.tools.FileAnalysisToolScreen
import com.openlysis.feature.tools.SmsAnalysisToolScreen
import com.openlysis.feature.tools.ToolsScreen
import com.openlysis.feature.tools.ToolsScreenViewModel
import com.openlysis.feature.tools.data.ToolCategory
import kotlinx.serialization.Serializable

/**
 * Base navigation route for the tools navigation graph.
 */
@Serializable
data object ToolsBaseRoute

/**
 * Route for accessing the tools screen.
 */
@Serializable
data object ToolsRoute

/**
 * Route for accessing the email analysis tool screen, which provides functionality
 * for analyzing email messages within the tools navigation graph.
 */
@Serializable
data object EmailAnalysisToolRoute

/**
 * Route for accessing the SMS analysis tool screen, which provides functionality
 * for analyzing SMS messages within the tools navigation graph.
 */
@Serializable
data object SmsAnalysisToolRoute

/**
 * Route for accessing the file analysis tool screen, which provides functionality
 * for analyzing files and documents within the tools navigation graph.
 */
@Serializable
data object FileAnalysisToolRoute

/**
 * Provides functionality to navigate to the tools screen.
 */
fun NavController.navigateToTools(navOptions: NavOptions) =
    navigate(ToolsBaseRoute, navOptions = navOptions)

/**
 * Adds the tools screen to the navigation graph.
 *
 * @param navController The navigation controller for handling navigation events
 * @param onMessageAnalysisStart Callback triggered when message analysis is started
 * @param onTopBarUpdate Callback to update the top bar state
 * @param enterTransition Animation played when the screen enters
 * @param exitTransition Animation played when the screen exits
 * @param popEnterTransition Animation played when the screen re-enters after pop (defaults to enterTransition)
 * @param popExitTransition Animation played when the screen is popped (defaults to exitTransition)
 */
fun NavGraphBuilder.toolsScreen(
    navController: NavController,
    onMessageAnalysisStart: (MessageAnalysis) -> Unit,
    onFileAnalysisStart: (FileMultiAnalysis) -> Unit,
    onTopBarUpdate: (TopBarState) -> Unit,
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
    navigation<ToolsBaseRoute>(startDestination = ToolsRoute) {
        composable<ToolsRoute>(
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition
        ) { backStackEntry ->
            ToolsScreen(
                onToolClick = {
                    when (it) {
                        ToolCategory.Email -> navController.navigate(EmailAnalysisToolRoute)
                        ToolCategory.Sms -> navController.navigate(SmsAnalysisToolRoute)
                        ToolCategory.File -> navController.navigate(FileAnalysisToolRoute)
                        ToolCategory.Url -> TODO()
                    }
                },
                viewModel = getSharedViewModel(backStackEntry, navController)
            )
        }

        composable<EmailAnalysisToolRoute> { backStackEntry ->
            EmailAnalysisToolScreen(
                viewModel = getSharedViewModel(backStackEntry, navController),
                onAnalysisStart = onMessageAnalysisStart,
                onTopBarUpdate = onTopBarUpdate
            )
        }

        composable<SmsAnalysisToolRoute> { backStackEntry ->
            SmsAnalysisToolScreen(
                viewModel = getSharedViewModel(backStackEntry, navController),
                onAnalysisStart = onMessageAnalysisStart,
                onTopBarUpdate = onTopBarUpdate
            )
        }

        composable<FileAnalysisToolRoute> { backStackEntry ->
            FileAnalysisToolScreen(
                viewModel = getSharedViewModel(backStackEntry, navController),
                onAnalysisStart = onFileAnalysisStart,
                onTopBarUpdate = onTopBarUpdate
            )
        }
    }
}

/**
 * Retrieves a shared [ToolsScreenViewModel] instance that persists across navigation within the tools graph.
 *
 * @param backStackEntry The current navigation back stack entry
 * @param navController The navigation controller used to retrieve the graph entry
 * @return A shared instance of [ToolsScreenViewModel]
 */
@Composable
private fun getSharedViewModel(
    backStackEntry: NavBackStackEntry,
    navController: NavController
): ToolsScreenViewModel {
    val graphBackStackEntry =
        remember(backStackEntry) {
            navController.getBackStackEntry(ToolsBaseRoute)
        }
    return hiltViewModel<ToolsScreenViewModel>(graphBackStackEntry)
}