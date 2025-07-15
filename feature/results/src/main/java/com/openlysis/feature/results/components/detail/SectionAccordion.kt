package com.openlysis.feature.results.components.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.feature.results.R

/**
 * An accordion section with a header and expandable content.
 *
 * @param title The title displayed in the header.
 * @param modifier Modifier for styling the accordion.
 * @param isPrimarySection Whether the section uses primary styling.
 * @param initiallyExpanded If true, the section starts expanded.
 * @param content The composable content shown when expanded.
 */
@Composable
internal fun SectionAccordion(
    title: String,
    modifier: Modifier = Modifier,
    isPrimarySection: Boolean = true,
    initiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(initiallyExpanded) }
    val iconDegrees by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "details_section_accordion_animation"
    )

    val headerShape = RoundedCornerShape(LocalAppRadius.current.value100)
    val backgroundColor =
        if (isPrimarySection) {
            LocalAppColorScheme.current.background.brand.tertiary
        } else {
            Color.Transparent
        }
    val foregroundColor =
        if (isPrimarySection) {
            LocalAppColorScheme.current.text.brand.onTertiary
        } else {
            LocalAppColorScheme.current.text.default.primary
        }
    val textStyle =
        if (isPrimarySection) {
            LocalAppTypography.current.title6
        } else {
            LocalAppTypography.current.bodyBaseStrong
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value100),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .clip(headerShape)
                    .background(color = backgroundColor, shape = headerShape)
                    .toggleable(
                        value = expanded,
                        onValueChange = { expanded = it },
                        role = Role.Button
                    ).padding(
                        vertical = LocalAppSpacing.current.value100,
                        horizontal = LocalAppSpacing.current.value200
                    )
        ) {
            Text(
                text = title,
                style = textStyle,
                color = foregroundColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = AppIcons.ChevronDown,
                contentDescription =
                    stringResource(
                        if (expanded) {
                            R.string.details_section_accordion_expanded_chevron_icon_alt
                        } else {
                            R.string.details_section_accordion_collapsed_chevron_icon_alt
                        }
                    ),
                tint = foregroundColor,
                modifier =
                    Modifier
                        .size(24.dp)
                        .rotate(iconDegrees)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
                modifier = Modifier.padding(horizontal = LocalAppSpacing.current.value300)
            ) {
                content()
            }
        }
    }
}