package com.openlysis.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.feature.auth.model.AuthenticationType
import com.openlysis.feature.auth.navigation.AuthBaseRoute
import com.openlysis.feature.auth.navigation.WelcomeRoute
import com.openlysis.feature.auth.navigation.navigateToAuthentication
import com.openlysis.feature.permission.navigation.NotificationsPermissionRoute
import com.openlysis.feature.permission.navigation.SmsPermissionRoute
import com.openlysis.feature.permission.navigation.navigateToNotificationsPermission
import com.openlysis.feature.permission.navigation.navigateToSmsPermission
import com.openlysis.feature.results.navigation.navigateToResults
import com.openlysis.feature.tools.navigation.navigateToTools
import com.openlysis.navigation.AppPermission
import com.openlysis.navigation.AuthDestination
import com.openlysis.navigation.RootDestination
import com.openlysis.navigation.TemporarySettings
import com.openlysis.navigation.TopLevelDestination

/**
 * Creates and remembers an instance of [AppState].
 *
 * @return An instance of [AppState] that is remembered across recompositions.
 */
@Composable
internal fun rememberAppState(): AppState {
    val navController = rememberNavController()
    val topBarState =
        remember {
            mutableStateOf(
                TopBarState(
                    title = "",
                    hasMenu = false
                )
            )
        }
    return remember(navController) {
        AppState(
            navController = navController,
            _topBarState = topBarState
        )
    }
}

/**
 * The overall state of the application.
 *
 * @property navController The [NavHostController] used for navigating between screens.
 */
@Stable
internal class AppState(
    val navController: NavHostController,
    private val _topBarState: MutableState<TopBarState>
) {
    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry =
                navController.currentBackStackEntryAsState()
            return currentEntry.value?.destination
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() =
            TopLevelDestination.entries.firstOrNull {
                currentDestination?.hasRoute(it.route) == true
            }

    val isAuthDestination: Boolean
        @Composable get() =
            currentDestination?.hierarchy?.any {
                it.hasRoute(AuthBaseRoute::class)
            } == true

    val isPermissionDestination: Boolean
        @Composable get() =
            currentDestination?.hierarchy?.any {
                it.hasRoute(SmsPermissionRoute::class) ||
                    (
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            it.hasRoute(NotificationsPermissionRoute::class)
                    )
            } == true

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val topBarState: State<TopBarState> = _topBarState

    /**
     * Updates the state of the top bar with the provided new state.
     *
     * @param newState The new [TopBarState] to be applied to the top bar.
     */
    fun updateTopBarState(newState: TopBarState) {
        _topBarState.value = newState
    }

    /**
     * Navigates to a top-level destination in the app.
     *
     * @param destination The [TopLevelDestination] to navigate to, which can be Tools, Results, or Settings.
     */
    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        val navOptions =
            navOptions {
                popUpTo(RootDestination.TopLevel.startRoute) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        when (destination) {
            TopLevelDestination.Tools -> navController.navigateToTools(navOptions)
            TopLevelDestination.Results -> navController.navigateToResults(navOptions)
            TopLevelDestination.Settings -> navController.navigate(TemporarySettings, navOptions)
        }
    }

    /**
     * Checks if the given destination is a top-level destination in the app's navigation hierarchy.
     *
     * @param destination The [NavDestination] to check.
     * @return `true` if the destination is a top-level destination, `false` otherwise.
     */
    fun isTopLevelDestination(destination: NavDestination): Boolean =
        topLevelDestinations.any {
            destination.hasRoute(it.route)
        }

    /**
     * Navigates to the specified authentication destination in the app.
     *
     * @param destination The [AuthDestination] to navigate to, such as Welcome, SignUp, or SignIn.
     */
    fun navigateToAuthDestination(destination: AuthDestination) {
        val navOptions =
            navOptions {
                popUpTo(RootDestination.Authentication.startRoute) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

        when (destination) {
            AuthDestination.Welcome ->
                navController.navigate(
                    route = WelcomeRoute,
                    navOptions = navOptions
                )
            AuthDestination.SignUp ->
                navController.navigateToAuthentication(
                    AuthenticationType.SignUp,
                    navOptions = navOptions
                )
            AuthDestination.SignIn ->
                navController.navigateToAuthentication(
                    AuthenticationType.SignIn,
                    navOptions = navOptions
                )
        }
    }

    /**
     * Navigates to the specified root destination in the app.
     *
     * @param root The [RootDestination] to navigate to.
     */
    fun navigateToRootDestination(root: RootDestination) {
        val navOptions =
            navOptions {
                popUpTo(navController.graph.id) {
                    inclusive = true
                    saveState = false
                }

                launchSingleTop = true
                restoreState = false
            }

        navController.navigate(route = root.startBaseRoute, navOptions = navOptions)
    }

    /**
     * Navigates to the permission request screen for the specified [AppPermission].
     *
     * @param appPermission The [AppPermission] to request from the user.
     */
    fun askForPermission(appPermission: AppPermission) {
        val navOptions =
            navOptions {
                launchSingleTop = true
            }

        when {
            appPermission == AppPermission.ReceiveSms ->
                navController.navigateToSmsPermission(navOptions)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                appPermission == AppPermission.PostNotifications -> {
                navController.navigateToNotificationsPermission(navOptions)
            }
        }
    }
}