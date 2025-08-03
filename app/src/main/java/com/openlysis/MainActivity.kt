package com.openlysis

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.navigation.RootDestination
import com.openlysis.ui.App
import com.openlysis.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }
        splashScreen.setOnExitAnimationListener { viewProvider ->
            val rotateIcon =
                try {
                    val iconRotation =
                        PropertyValuesHolder.ofFloat(
                            View.ROTATION,
                            viewProvider.iconView.rotation,
                            360f
                        )

                    ObjectAnimator
                        .ofPropertyValuesHolder(
                            viewProvider.iconView,
                            iconRotation
                        ).apply {
                            interpolator = AccelerateDecelerateInterpolator()
                            duration = 400

                            doOnStart {
                                viewProvider.iconView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                            }

                            doOnEnd {
                                viewProvider.iconView.setLayerType(View.LAYER_TYPE_NONE, null)
                            }
                        }
                } catch (_: NullPointerException) {
                    null
                }

            val splashTranslation =
                PropertyValuesHolder.ofFloat(
                    View.TRANSLATION_Y,
                    viewProvider.view.translationY,
                    viewProvider.view.height.toFloat()
                )
            val slideSplashDown =
                ObjectAnimator
                    .ofPropertyValuesHolder(
                        viewProvider.view,
                        splashTranslation
                    ).apply {
                        interpolator = AccelerateDecelerateInterpolator()
                        duration = 200
                    }

            if (rotateIcon != null) {
                val animatorSet = AnimatorSet()
                animatorSet.play(rotateIcon).before(slideSplashDown)
                animatorSet.doOnEnd { viewProvider.remove() }
                animatorSet.start()
            } else {
                slideSplashDown.doOnEnd { viewProvider.remove() }
                slideSplashDown.start()
            }
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val appState = rememberAppState()

            LaunchedEffect(
                uiState
            ) {
                if (uiState !is MainActivityUiState.Success) return@LaunchedEffect
                val successUiState = uiState as MainActivityUiState.Success

                if (!uiState.isUserSignedIn()) {
                    appState.navigateToRootDestination(RootDestination.Authentication)
                    return@LaunchedEffect
                }

                successUiState.pendingPermissions.forEach {
                    appState.askForPermission(it)
                }
            }

            OpenlysisTheme {
                App(appState)
            }
        }
    }
}