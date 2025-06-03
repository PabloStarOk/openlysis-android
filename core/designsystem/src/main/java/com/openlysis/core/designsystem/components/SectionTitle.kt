package com.openlysis.core.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppDepth
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = LocalAppDepth.current.value050,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = title,
            modifier =
                modifier.padding(
                    horizontal = LocalAppSpacing.current.value400,
                    vertical = LocalAppSpacing.current.value200
                ),
            style = MaterialTheme.typography.titleMedium,
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