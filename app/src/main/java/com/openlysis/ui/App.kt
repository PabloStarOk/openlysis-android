package com.openlysis.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
@Composable
internal fun App(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = LocalAppColorScheme.current.background.default.primary,
        bottomBar = {
            AppNavBar(appState)
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