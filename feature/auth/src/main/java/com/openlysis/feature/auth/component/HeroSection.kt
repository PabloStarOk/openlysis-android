package com.openlysis.feature.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.auth.R

/**
 * Displays the hero section for the authentication screens.
 *
 * @param iconInlineVariant Whether to display a variant where the icon is inlined with the title.
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
internal fun HeroSection(
    iconInlineVariant: Boolean,
    modifier: Modifier = Modifier
) {
    val iconSize = if (iconInlineVariant) 75.dp else 150.dp
    val title = stringResource(R.string.openlysis)
    val formattedTitle = if (iconInlineVariant) title.substring(1) else title
    val modifier = modifier.padding(vertical = LocalAppSpacing.current.value800)

    if (iconInlineVariant) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            AppIcon(modifier = Modifier.size(iconSize))
            Title(title = formattedTitle)
        }
    } else {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(
                    space = LocalAppSpacing.current.value400,
                    alignment = Alignment.CenterVertically
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            AppIcon(modifier = Modifier.size(iconSize))
            Title(title = formattedTitle)
        }
    }
}

@Composable
private fun AppIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = AppIcons.Openlysis,
        contentDescription = stringResource(R.string.openlysis_icon_alt),
        tint = LocalAppColorScheme.current.icon.brand.onPrimary,
        modifier = modifier
    )
}

@Composable
private fun Title(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = LocalAppColorScheme.current.text.brand.onPrimary,
        style = LocalAppTypography.current.title1,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview
@Composable
private fun HeroSectionPreview() {
    OpenlysisTheme {
        HeroSection(
            iconInlineVariant = false
        )
    }
}