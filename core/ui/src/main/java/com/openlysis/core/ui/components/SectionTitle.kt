package com.openlysis.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.openlysis.core.ui.theme.OpenlysisTheme
import com.openlysis.core.ui.theme.size.Depth
import com.openlysis.core.ui.theme.size.Spacing

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shadowElevation = Depth.Value050,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = title,
            modifier =
                modifier.padding(
                    horizontal = Spacing.Value400,
                    vertical = Spacing.Value200
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