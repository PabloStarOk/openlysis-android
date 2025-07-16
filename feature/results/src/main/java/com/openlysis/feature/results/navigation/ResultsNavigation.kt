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
import com.openlysis.feature.results.FileMultiAnalysisDetailsScreen
import com.openlysis.feature.results.FileMultiAnalysisDetailsScreenViewModel
import com.openlysis.feature.results.FileMultiAnalysisPreviewsScreenViewModel
import com.openlysis.feature.results.MessageAnalysisDetailsScreen
import com.openlysis.feature.results.MessageDetailsScreenViewModel
import com.openlysis.feature.results.PreviewsScreen
import com.openlysis.feature.results.R
import com.openlysis.feature.results.ResultsScreen
import com.openlysis.feature.results.SmsPreviewsScreenViewModel
import com.openlysis.feature.results.UrlMultiAnalysisDetailsScreen
import com.openlysis.feature.results.UrlMultiAnalysisDetailsScreenViewModel
import com.openlysis.feature.results.UrlMultiAnalysisPreviewsScreenViewModel
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
 * Route for accessing the analysis previews screen.
 *
 * @property type A [ResultType] representing the type of result to preview.
 */
@Serializable
data class PreviewsRoute(
    val type: ResultType
)

/**
 * Route for accessing the message analysis details screen.
 */
@Serializable
data class MessageAnalysisDetailsRoute(
    val analysisId: String,
    val messageType: MessageType
)

/**
 * Route for accessing the file multi analysis details screen.
 */
@Serializable
data class FileMultiAnalysisDetailsRoute(
    val analysisId: String
)

/**
 * Route for accessing the URL multi analysis details screen.
 */
@Serializable
data class UrlMultiAnalysisDetailsRoute(
    val analysisId: String
)

/**
 * Provides functionality to navigate to the analysis results screen.
 */
fun NavController.navigateToResults(navOptions: NavOptions) =
    this.navigate(ResultsBaseRoute, navOptions = navOptions)

/**
 * Provides functionality to navigate to the analysis previews screen.
 */
fun NavController.navigateToPreviews(type: ResultType) = this.navigate(PreviewsRoute(type))

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
 * Navigates to the file multi analysis details screen.
 *
 * @receiver NavController used for navigation.
 * @param analysisId The unique identifier of the file analysis.
 */
fun NavController.navigateToFileMultiAnalysisDetails(analysisId: String) {
    this.navigate(FileMultiAnalysisDetailsRoute(analysisId))
}

/**
 * Navigates to the URL multi analysis details screen.
 *
 * @receiver NavController used for navigation.
 * @param analysisId The unique identifier of the URL analysis.
 */
fun NavController.navigateToUrlMultiAnalysisDetails(analysisId: String) {
    this.navigate(UrlMultiAnalysisDetailsRoute(analysisId))
}

/**
 * Adds the analysis results screens to the navigation graph.
 *
 * @param onTopBarUpdate Callback to update top bar for screens.
 * @param onResultsCardClick Callback to invoke when an analysis results card is clicked.
 * @param onPreviewDetailsClick Callback to invoke when a preview card's details button is clicked.
 * @param enterTransition Animation played when the screen enters
 * @param exitTransition Animation played when the screen exits
 * @param popEnterTransition Animation played when the screen re-enters after pop (defaults to enterTransition)
 * @param popExitTransition Animation played when the screen is popped (defaults to exitTransition)
 */
fun NavGraphBuilder.resultsScreen(
    onTopBarUpdate: (TopBarState) -> Unit,
    onResultsCardClick: (ResultType) -> Unit,
    onPreviewDetailsClick: (String, ResultType) -> Unit,
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
                onEmailResultsClick = { onResultsCardClick(ResultType.Email) },
                onSmsResultsClick = { onResultsCardClick(ResultType.Sms) },
                onFileResultsClick = { onResultsCardClick(ResultType.File) },
                onUrlResultsClick = { onResultsCardClick(ResultType.Url) }
            )
        }

        composable<PreviewsRoute>(
            enterTransition = {
                this.previewsScreenEnterTransition(
                    MessageAnalysisDetailsRoute::class,
                    FileMultiAnalysisDetailsRoute::class,
                    UrlMultiAnalysisDetailsRoute::class
                )
            },
            exitTransition = {
                this.previewsScreenExitTransition(
                    MessageAnalysisDetailsRoute::class,
                    FileMultiAnalysisDetailsRoute::class,
                    UrlMultiAnalysisDetailsRoute::class
                )
            }
        ) { backStackEntry ->
            val route: PreviewsRoute = backStackEntry.toRoute()
            val screenData = previewDataMap.getValue(route.type)
            PreviewsScreen(
                viewModel = screenData.getViewModel(),
                onTopBarUpdate = onTopBarUpdate,
                screenTitle = stringResource(screenData.screenTitleResId),
                previewCardHeaderLabel = stringResource(screenData.previewsHeaderLabelResId),
                onPreviewDetailsClick = { onPreviewDetailsClick(it, route.type) }
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

        composable<FileMultiAnalysisDetailsRoute>(
            enterTransition = { slideIntoContainer(towards = SlideDirection.Down) + fadeIn() },
            exitTransition = { slideOutOfContainer(towards = SlideDirection.Up) + fadeOut() }
        ) { backStackEntry ->
            val route: FileMultiAnalysisDetailsRoute = backStackEntry.toRoute()
            FileMultiAnalysisDetailsScreen(
                viewModel = hiltViewModel<FileMultiAnalysisDetailsScreenViewModel>(),
                onTopBarUpdate = onTopBarUpdate,
                analysisId = route.analysisId
            )
        }

        composable<UrlMultiAnalysisDetailsRoute>(
            enterTransition = { slideIntoContainer(towards = SlideDirection.Down) + fadeIn() },
            exitTransition = { slideOutOfContainer(towards = SlideDirection.Up) + fadeOut() }
        ) { backStackEntry ->
            val route: UrlMultiAnalysisDetailsRoute = backStackEntry.toRoute()
            UrlMultiAnalysisDetailsScreen(
                viewModel = hiltViewModel<UrlMultiAnalysisDetailsScreenViewModel>(),
                onTopBarUpdate = onTopBarUpdate,
                analysisId = route.analysisId
            )
        }
    }
}

private val previewDataMap =
    mapOf(
        Pair(
            ResultType.Email,
            PreviewsScreenData(
                getViewModel = { hiltViewModel<EmailPreviewsScreenViewModel>() },
                screenTitleResId = R.string.email_previews_screen_title,
                previewsHeaderLabelResId = R.string.email_previews_cards_header_label
            )
        ),
        Pair(
            ResultType.Sms,
            PreviewsScreenData(
                getViewModel = { hiltViewModel<SmsPreviewsScreenViewModel>() },
                screenTitleResId = R.string.sms_previews_screen_title,
                previewsHeaderLabelResId = R.string.sms_previews_cards_header_label
            )
        ),
        Pair(
            ResultType.File,
            PreviewsScreenData(
                getViewModel = { hiltViewModel<FileMultiAnalysisPreviewsScreenViewModel>() },
                screenTitleResId = R.string.file_previews_screen_title,
                previewsHeaderLabelResId = R.string.file_previews_cards_header_label
            )
        ),
        Pair(
            ResultType.Url,
            PreviewsScreenData(
                getViewModel = { hiltViewModel<UrlMultiAnalysisPreviewsScreenViewModel>() },
                screenTitleResId = R.string.url_previews_screen_title,
                previewsHeaderLabelResId = R.string.url_previews_cards_header_label
            )
        )
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.previewsScreenEnterTransition(
    vararg initialRoutes: KClass<*>
): @JvmSuppressWildcards EnterTransition? {
    val slideDirection =
        if (initialRoutes.any { this.initialState.destination.hasRoute(it) }) {
            SlideDirection.Up
        } else {
            SlideDirection.Down
        }
    return slideIntoContainer(towards = slideDirection) + fadeIn()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.previewsScreenExitTransition(
    vararg targetRoutes: KClass<*>
): @JvmSuppressWildcards ExitTransition? {
    val slideDirection =
        if (targetRoutes.any { this.targetState.destination.hasRoute(it) }) {
            SlideDirection.Down
        } else {
            SlideDirection.Up
        }
    return slideOutOfContainer(towards = slideDirection) + fadeOut()
}