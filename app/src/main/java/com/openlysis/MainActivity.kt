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
import androidx.compose.runtime.getValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.theme.OpenlysisTheme
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
            val iconRotation =
                PropertyValuesHolder.ofFloat(View.ROTATION, viewProvider.iconView.rotation, 360f)
            val rotateIcon =
                ObjectAnimator
                    .ofPropertyValuesHolder(
                        viewProvider.iconView,
                        iconRotation
                    ).apply {
                        interpolator = AccelerateDecelerateInterpolator()
                        duration = 400
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

            val animatorSet = AnimatorSet()
            animatorSet.play(rotateIcon).before(slideSplashDown)
            animatorSet.doOnEnd { viewProvider.remove() }
            animatorSet.start()
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val appState = rememberAppState(isUserSignedIn = uiState.isUserSignedIn())
            OpenlysisTheme {
                App(appState)
            }
        }
    }
}