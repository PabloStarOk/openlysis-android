package com.openlysis.feature.auth.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.openlysis.feature.auth.AuthScreen
import com.openlysis.feature.auth.AuthScreenViewModel
import com.openlysis.feature.auth.WelcomeScreen
import com.openlysis.feature.auth.model.AuthenticationType
import kotlinx.serialization.Serializable

/**
 * Base navigation route for the authentication navigation graph.
 */
@Serializable
data object AuthBaseRoute

/**
 * Route for accessing the welcome screen.
 */
@Serializable
data object WelcomeRoute

/**
 * Route for the authentication screen.
 *
 * @property authenticationType The type of authentication to use (e.g., sign in, sign up).
 */
@Serializable
data class AuthRoute(
    val authenticationType: AuthenticationType
)

/**
 * Navigates to the sign-in screen.
 *
 * @param authenticationType The type of authentication to use.
 * @param navOptions Navigation options for the transition.
 */
fun NavController.navigateToAuthentication(
    authenticationType: AuthenticationType,
    navOptions: NavOptions
) = this.navigate(AuthRoute(authenticationType), navOptions = navOptions)

/**
 * Adds the authentication screens to the navigation graph.
 *
 * @param onGoToSignUp Callback invoked when the user requests to go to sign-up screen.
 * @param onGoToSignIn Callback invoked when the user requests to go to sign-in screen.
 * @param onAuthenticated Callback invoked when the user is successfully authenticated.
 */
fun NavGraphBuilder.authScreen(
    onGoToSignUp: () -> Unit,
    onGoToSignIn: () -> Unit,
    onAuthenticated: () -> Unit
) {
    navigation<AuthBaseRoute>(startDestination = WelcomeRoute) {
        composable<WelcomeRoute>(
            enterTransition = { slideIntoContainer(SlideDirection.Right) },
            exitTransition = { slideOutOfContainer(SlideDirection.Left) }
        ) {
            WelcomeScreen(onGoToSignUp, onGoToSignIn)
        }

        composable<AuthRoute>(
            enterTransition = {
                val direction =
                    if (this.initialState.destination.hasRoute<WelcomeRoute>()) {
                        SlideDirection.Left
                    } else {
                        SlideDirection.Right
                    }
                slideIntoContainer(direction)
            },
            exitTransition = {
                val direction =
                    if (this.targetState.destination.hasRoute<WelcomeRoute>()) {
                        SlideDirection.Right
                    } else {
                        SlideDirection.Left
                    }
                slideOutOfContainer(direction)
            }
        ) { backStackEntry ->
            val route: AuthRoute = backStackEntry.toRoute()
            AuthScreen(
                viewModel =
                    hiltViewModel<AuthScreenViewModel, AuthScreenViewModel.Factory> { factory ->
                        factory.create(route.authenticationType)
                    },
                onAuthenticated = onAuthenticated
            )
        }
    }
}