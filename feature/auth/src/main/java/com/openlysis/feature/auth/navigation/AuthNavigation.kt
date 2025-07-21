package com.openlysis.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
 * Adds the authentication screens to the navigation graph.
 *
 * @param onSignUpRequest Callback invoked when the user requests to sign up.
 * @param onSignInRequest Callback invoked when the user requests to sign in.
 */
fun NavGraphBuilder.authScreen(
    onSignUpRequest: () -> Unit,
    onSignInRequest: () -> Unit
) {
    navigation<AuthBaseRoute>(startDestination = WelcomeRoute) {
        composable<WelcomeRoute> {
            WelcomeScreen(onSignUpRequest, onSignInRequest)
        }
    }
}