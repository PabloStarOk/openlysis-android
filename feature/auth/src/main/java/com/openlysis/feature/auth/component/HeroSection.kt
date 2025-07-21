package com.openlysis.feature.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.auth.R

/**
 * Displays the hero section for the authentication screens.
 *
 * @param showIcon Whether to display the Openlysis icon.
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
internal fun HeroSection(
    showIcon: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                space = LocalAppSpacing.current.value400,
                alignment = Alignment.CenterVertically
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .padding(vertical = LocalAppSpacing.current.value1600)
    ) {
        if (showIcon) {
            Icon(
                imageVector = AppIcons.Openlysis,
                contentDescription = stringResource(R.string.openlysis_icon_alt),
                tint = LocalAppColorScheme.current.icon.brand.onPrimary,
                modifier = Modifier.size(150.dp)
            )
        }

        Text(
            text = stringResource(R.string.openlysis),
            color = LocalAppColorScheme.current.text.brand.onPrimary,
            style = LocalAppTypography.current.title1,
            textAlign = TextAlign.Center
        )
    }
}