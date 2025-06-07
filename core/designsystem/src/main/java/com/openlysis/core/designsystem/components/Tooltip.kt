package com.openlysis.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A tooltip with a list of messages.
 *
 * @param messages An array of strings representing the messages to be displayed in the tooltip.
 * @param visible A boolean indicating whether the tooltip should be visible or not.
 * @param onDismissRequest A lambda function to be invoked when the tooltip is dismissed.
 * @param modifier A [Modifier] to be applied to the tooltip.
 */
@Composable
fun Tooltip(
    messages: Array<AnnotatedString>,
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transitionState = remember { MutableTransitionState(visible) }
    transitionState.targetState = visible

    if (transitionState.currentState || transitionState.targetState) {
        Popup(
            onDismissRequest = onDismissRequest,
            properties =
                PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
        ) {
            AnimatedVisibility(
                visibleState = transitionState,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            LocalAppSpacing.current.value200
                        ),
                    modifier =
                        modifier
                            .widthIn(max = 200.dp)
                            .clip(RoundedCornerShape(LocalAppRadius.current.value100))
                            .background(
                                color = LocalAppColorScheme.current.background.default.primary
                            ).border(
                                width = 1.dp,
                                color = LocalAppColorScheme.current.border.default.primary,
                                shape = RoundedCornerShape(LocalAppRadius.current.value100)
                            ).padding(all = LocalAppSpacing.current.value300)
                ) {
                    messages.forEachIndexed { index, message ->
                        Text(
                            text = message,
                            color = LocalAppColorScheme.current.text.default.primary,
                            style = LocalAppTypography.current.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (index < messages.lastIndex) {
                            HorizontalDivider(
                                color = LocalAppColorScheme.current.border.default.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TooltipPreview() {
    OpenlysisTheme(darkTheme = false) {
        Tooltip(
            messages =
                arrayOf(
                    AnnotatedString("Example message."),
                    AnnotatedString("Example message 2.")
                ),
            visible = true,
            onDismissRequest = { }
        )
    }
}