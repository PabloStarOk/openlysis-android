package com.openlysis.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.components.bar.TopBar
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.navigation.AppNavBar
import com.openlysis.navigation.AppNavHost

/**
 * Composable function for the main application UI.
 *
 * @param appState The current state of the application, used to manage navigation and other app-wide concerns.
 * @param modifier Optional Modifier to be applied to the root Scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun App(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    val isAuthDestination = appState.isAuthDestination
    val isTopLevelDestination = appState.currentTopLevelDestination != null
    val isPermissionDestination = appState.isPermissionDestination
    val density = LocalDensity.current

    val topBarVisible = remember { MutableTransitionState(false) }
    val navBarVisible = remember { MutableTransitionState(false) }

    LaunchedEffect(isTopLevelDestination, isAuthDestination, isPermissionDestination) {
        topBarVisible.targetState = !isTopLevelDestination &&
            !isAuthDestination &&
            !isPermissionDestination
        navBarVisible.targetState = isTopLevelDestination
    }

    Scaffold(
        containerColor = LocalAppColorScheme.current.background.default.primary,
        topBar = {
            val topBarWindowInsets = TopAppBarDefaults.windowInsets
            val topBarInset =
                remember(density, topBarWindowInsets) {
                    topBarWindowInsets.getTop(density)
                }

            AnimatedVisibility(
                visibleState = topBarVisible,
                enter =
                    slideInVertically(initialOffsetY = { -it }) +
                        expandVertically(initialHeight = { topBarInset }),
                exit =
                    slideOutVertically(targetOffsetY = { -it }) +
                        shrinkVertically(targetHeight = { topBarInset })
            ) {
                TopBar(
                    onBackClick = { navController.popBackStack() },
                    state = appState.topBarState.value
                )
            }
        },
        bottomBar = {
            val bottomBarWindowInsets = BottomAppBarDefaults.windowInsets
            val bottomBarInset =
                remember(density, bottomBarWindowInsets) {
                    bottomBarWindowInsets.getBottom(density)
                }

            AnimatedVisibility(
                visibleState = navBarVisible,
                enter =
                    slideInVertically(initialOffsetY = { it }) +
                        expandVertically(initialHeight = { bottomBarInset }),
                exit =
                    slideOutVertically(targetOffsetY = { it }) +
                        shrinkVertically(targetHeight = { bottomBarInset })
            ) {
                AppNavBar(appState)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        AppNavHost(
            appState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AppPreview() {
    OpenlysisTheme(darkTheme = false) {
        App(rememberAppState())
    }
}