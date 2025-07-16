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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.data.analysis.model.message.MessageType
import com.openlysis.feature.results.EmailPreviewsScreenViewModel
import com.openlysis.feature.results.MessageAnalysisDetailsScreen
import com.openlysis.feature.results.MessageDetailsScreenViewModel
import com.openlysis.feature.results.PreviewsScreen
import com.openlysis.feature.results.R
import com.openlysis.feature.results.ResultsScreen
import com.openlysis.feature.results.SmsPreviewsScreenViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

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
 * Route for accessing the SMS analysis previews screen.
 */
@Serializable
data object SmsAnalysisPreviewsRoute

/**
 * Route for accessing the message analysis details screen.
 */
@Serializable
data class MessageAnalysisDetailsRoute(
    val analysisId: String,
    val messageType: MessageType
)

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
 * Provides functionality to navigate to the SMS analysis previews screen.
 */
fun NavController.navigateToSmsAnalysisPreviews() = this.navigate(SmsAnalysisPreviewsRoute)

/**
 * Navigates to the message analysis details screen.
 *
 * @receiver NavController used for navigation.
 * @param analysisId The unique identifier of the analysis.
 * @param messageType The type of the analyzed message.
 */
fun NavController.navigateToMessageAnalysisDetails(
    analysisId: String,
    messageType: MessageType
) {
    this.navigate(MessageAnalysisDetailsRoute(analysisId, messageType))
}

/**
 * Adds the analysis results screens to the navigation graph.
 *
 * @param onTopBarUpdate Callback to update top bar for screens.
 * @param onEmailResultsClick Callback to invoke when the email analysis results card is clicked.
 * @param onSmsResultsClick Callback to invoke when the SMS analysis results card is clicked.
 * @param onMessagePreviewDetailsClick Callback to invoke when a message preview card is clicked, receives the analysis ID.
 * @param enterTransition Animation played when the screen enters
 * @param exitTransition Animation played when the screen exits
 * @param popEnterTransition Animation played when the screen re-enters after pop (defaults to enterTransition)
 * @param popExitTransition Animation played when the screen is popped (defaults to exitTransition)
 */
fun NavGraphBuilder.resultsScreen(
    onTopBarUpdate: (TopBarState) -> Unit,
    onEmailResultsClick: () -> Unit,
    onSmsResultsClick: () -> Unit,
    onMessagePreviewDetailsClick: (String, MessageType) -> Unit,
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
                onSmsResultsClick = onSmsResultsClick,
                onFileResultsClick = { },
                onUrlResultsClick = { }
            )
        }

        composable<EmailAnalysisPreviewsRoute>(
            enterTransition = {
                this.previewsScreenEnterTransition(MessageAnalysisDetailsRoute::class)
            },
            exitTransition = {
                this.previewsScreenExitTransition(MessageAnalysisDetailsRoute::class)
            }
        ) {
            PreviewsScreen(
                viewModel = hiltViewModel<EmailPreviewsScreenViewModel>(),
                onTopBarUpdate = onTopBarUpdate,
                screenTitle = stringResource(R.string.email_previews_screen_title),
                previewCardHeaderLabel = stringResource(R.string.email_previews_cards_header_label),
                onPreviewDetailsClick = { onMessagePreviewDetailsClick(it, MessageType.Email) }
            )
        }

        composable<SmsAnalysisPreviewsRoute>(
            enterTransition = {
                this.previewsScreenEnterTransition(MessageAnalysisDetailsRoute::class)
            },
            exitTransition = {
                this.previewsScreenExitTransition(MessageAnalysisDetailsRoute::class)
            }
        ) {
            PreviewsScreen(
                viewModel = hiltViewModel<SmsPreviewsScreenViewModel>(),
                onTopBarUpdate = onTopBarUpdate,
                screenTitle = stringResource(R.string.sms_previews_screen_title),
                previewCardHeaderLabel = stringResource(R.string.sms_previews_cards_header_label),
                onPreviewDetailsClick = { onMessagePreviewDetailsClick(it, MessageType.Sms) }
            )
        }

        composable<MessageAnalysisDetailsRoute>(
            enterTransition = { slideIntoContainer(towards = SlideDirection.Down) + fadeIn() },
            exitTransition = { slideOutOfContainer(towards = SlideDirection.Up) + fadeOut() }
        ) { backStackEntry ->
            val route: MessageAnalysisDetailsRoute = backStackEntry.toRoute()
            val screenTitleResId =
                if (route.messageType == MessageType.Email) {
                    R.string.details_screen_email_title
                } else {
                    R.string.details_screen_sms_title
                }
            MessageAnalysisDetailsScreen(
                viewModel = hiltViewModel<MessageDetailsScreenViewModel>(),
                onTopBarUpdate = onTopBarUpdate,
                screenTitle = stringResource(screenTitleResId),
                analysisId = route.analysisId
            )
        }
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.previewsScreenEnterTransition(
    initialRoute: KClass<*>
): @JvmSuppressWildcards EnterTransition? {
    val slideDirection =
        if (this.initialState.destination.hasRoute(initialRoute)) {
            SlideDirection.Up
        } else {
            SlideDirection.Down
        }
    return slideIntoContainer(towards = slideDirection) + fadeIn()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.previewsScreenExitTransition(
    targetRoute: KClass<*>
): @JvmSuppressWildcards ExitTransition? {
    val slideDirection =
        if (this.targetState.destination.hasRoute(targetRoute)) {
            SlideDirection.Down
        } else {
            SlideDirection.Up
        }
    return slideOutOfContainer(towards = slideDirection) + fadeOut()
}