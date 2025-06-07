package com.openlysis.core.designsystem.components.alert

import com.openlysis.core.designsystem.R

/**
 * A map that associates each [AlertType] with its corresponding [AlertTypeData].
 */
internal val AlertTypeDataMap =
    mapOf<AlertType, AlertTypeData>(
        Pair(
            AlertType.Warning,
            AlertTypeData(
                getBackgroundColor = { scheme -> scheme.background.warning.secondary },
                getForegroundColor = { scheme -> scheme.text.warning.onSecondary },
                iconResId = R.drawable.alert_triangle_icon,
                iconAlt = R.string.warning_alert_icon_alt
            )
        ),
        Pair(
            AlertType.Danger,
            AlertTypeData(
                getBackgroundColor = { scheme -> scheme.background.danger.secondary },
                getForegroundColor = { scheme -> scheme.text.danger.onSecondary },
                iconResId = R.drawable.alert_octagon_icon,
                iconAlt = R.string.danger_alert_icon_alt
            )
        )
    )