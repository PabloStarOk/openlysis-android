package com.openlysis.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppDepth
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography

/**
 * A title to present a section on a screen.
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = LocalAppDepth.current.value050,
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        color = LocalAppColorScheme.current.background.brand.tertiary
    ) {
        Text(
            text = title,
            modifier =
                modifier
                    .padding(
                        horizontal = LocalAppSpacing.current.value400,
                        vertical = LocalAppSpacing.current.value200
                    ),
            style = LocalAppTypography.current.title6,
            color = LocalAppColorScheme.current.text.brand.onTertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SectionTitlePreview() {
    OpenlysisTheme(darkTheme = false) {
        SectionTitle("Label")
    }
}