package com.openlysis.navigation

import com.openlysis.feature.auth.navigation.AuthRoute
import com.openlysis.feature.auth.navigation.WelcomeRoute
import kotlin.reflect.KClass

/**
 * Represents the authentication-level destinations in the application's navigation.
 *
 * @property route The KClass of the route associated with the destination.
 */
internal enum class AuthDestination(
    val route: KClass<*>
) {
    /**
     * Destination for the welcome screen.
     */
    Welcome(route = WelcomeRoute::class),

    /**
     * Destination for the sign-up screen.
     */
    SignUp(route = AuthRoute::class),

    /**
     * Destination for the sign-in screen.
     */
    SignIn(route = AuthRoute::class)
}