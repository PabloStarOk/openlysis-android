package com.openlysis.feature.permission.navigation

import android.Manifest
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.feature.permission.PermissionScreen
import com.openlysis.feature.permission.R
import kotlinx.serialization.Serializable

/**
 * Route for accessing the SMS permission screen.
 */
@Serializable
data object SmsPermissionRoute

/**
 * Navigates to the SMS permission screen using the provided [NavOptions].
 *
 * @param navOptions Navigation options to customize the navigation behavior.
 */
fun NavController.navigateToSmsPermission(navOptions: NavOptions) =
    this.navigate(SmsPermissionRoute, navOptions)

/**
 * Adds the SMS permission screen to the navigation graph.
 *
 * @param onSmsPermissionAllowed Callback invoked when the SMS permission is granted by the user.
 * @param onSmsSkipPermission Callback invoked when the user chooses to skip or deny granting the SMS permission.
 * @param enterTransition Transition to use when entering the screen.
 * @param exitTransition Transition to use when exiting the screen.
 * @param popEnterTransition Transition to use when re-entering the screen via back navigation. Defaults to [enterTransition].
 * @param popExitTransition Transition to use when popping the screen from the back stack. Defaults to [exitTransition].
 */
fun NavGraphBuilder.permissionsScreen(
    onSmsPermissionAllowed: () -> Unit,
    onSmsSkipPermission: () -> Unit,
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
    composable<SmsPermissionRoute>(
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    ) {
        PermissionScreen(
            onPermissionAllowed = onSmsPermissionAllowed,
            onSkipRequest = onSmsSkipPermission,
            permission = Manifest.permission.RECEIVE_SMS,
            heroIcon = AppIcons.Sms,
            heroIconAlt = stringResource(R.string.screen_permission_sms_hero_icon_alt),
            heroTitle = stringResource(R.string.screen_permission_sms_hero_title),
            heroDescription = stringResource(R.string.screen_permission_sms_hero_description),
            illustration = ImageVector.vectorResource(R.drawable.illustration_sms),
            illustrationAlt = stringResource(R.string.screen_permission_sms_illustration_alt),
            allowLabel = stringResource(R.string.screen_permission_sms_allow_button_label),
            skipLabel = stringResource(R.string.screen_permission_sms_skip_button_label)
        )
    }
}