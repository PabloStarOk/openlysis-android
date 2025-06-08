package com.openlysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.ui.App
import com.openlysis.ui.rememberAppState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            OpenlysisTheme {
                App(appState)
            }
        }
    }
}