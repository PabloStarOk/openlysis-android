package com.openlysis.feature.results.components.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.feature.results.R

/**
 * A refresh button that displays a loading indicator as feedback.
 *
 * @param onRefreshClick Callback invoked when the refresh button is clicked
 * @param isRefreshing Boolean indicating whether the refresh action is in progress
 * @param displayLabel Boolean determining if the button label should be shown
 * @param modifier [Modifier] to be applied to the component
 * @param size The size of the button.
 */
@Composable
internal fun RefreshButton(
    onRefreshClick: () -> Unit,
    isRefreshing: Boolean,
    displayLabel: Boolean,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.Secondary,
    size: SizeType = SizeType.Default
) {
    val loadingIndicatorSize =
        when (size) {
            SizeType.Large -> 56.dp
            SizeType.Default -> 48.dp
            SizeType.Small -> 40.dp
            SizeType.ExtraSmall -> 32.dp
        }

    AnimatedContent(
        targetState = isRefreshing,
        transitionSpec = {
            scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) +
                fadeIn(
                    animationSpec = tween(300)
                ) togetherWith scaleOut(
                    targetScale = 1.0f,
                    animationSpec = tween(200, easing = FastOutLinearInEasing)
                ) +
                fadeOut(
                    animationSpec = tween(200)
                )
        },
        modifier = modifier
    ) { state ->
        if (state) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.size(loadingIndicatorSize)
            ) {
                CircularProgressIndicator(
                    color = LocalAppColorScheme.current.icon.brand.primary,
                    trackColor = LocalAppColorScheme.current.border.default.primary
                )
            }
        } else {
            AppButton(
                type = type,
                size = size,
                onClick = onRefreshClick,
                displayLabel = displayLabel,
                displayIcon = true,
                label =
                    stringResource(
                        R.string.analysis_preview_refresh_button_label
                    ),
                icon = AppIcons.Refresh,
                iconAlt =
                    stringResource(
                        R.string.analysis_preview_refresh_button_icon_alt
                    )
            )
        }
    }
}

@Preview
@Composable
private fun ActionButtonsPreview() {
    OpenlysisTheme {
        RefreshButton(
            onRefreshClick = { },
            isRefreshing = false,
            displayLabel = false
        )
    }
}