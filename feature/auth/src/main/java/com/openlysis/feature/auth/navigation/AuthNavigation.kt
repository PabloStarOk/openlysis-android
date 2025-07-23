package com.openlysis.feature.auth.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.openlysis.feature.auth.AuthScreen
import com.openlysis.feature.auth.AuthScreenViewModel
import com.openlysis.feature.auth.AuthenticationType
import com.openlysis.feature.auth.WelcomeScreen
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
 */
fun NavController.navigateToAuthentication(authenticationType: AuthenticationType) =
    this.navigate(AuthRoute(authenticationType))

/**
 * Adds the authentication screens to the navigation graph.
 *
 * @param onSignUpRequest Callback invoked when the user requests to sign up.
 * @param onSignInRequest Callback invoked when the user requests to sign in.
 * @param onAuthenticated Callback invoked when the user is successfully authenticated.
 */
fun NavGraphBuilder.authScreen(
    onSignUpRequest: () -> Unit,
    onSignInRequest: () -> Unit,
    onAuthenticated: () -> Unit
) {
    navigation<AuthBaseRoute>(startDestination = WelcomeRoute) {
        composable<WelcomeRoute>(
            enterTransition = { slideIntoContainer(SlideDirection.Right) },
            exitTransition = { slideOutOfContainer(SlideDirection.Left) }
        ) {
            WelcomeScreen(onSignUpRequest, onSignInRequest)
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