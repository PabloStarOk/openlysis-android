package com.openlysis.core.designsystem.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.openlysis.core.designsystem.modifier.SizeType

/**
 * A map that associates each [SizeType] with its corresponding [ButtonSize].
 */
internal val ButtonSizeTypeMap =
    mapOf<SizeType, ButtonSize>(
        Pair(
            SizeType.Default,
            ButtonSize(
                getPadding = { spacing ->
                    PaddingValues(
                        vertical = spacing.value200,
                        horizontal = spacing.value300
                    )
                },
                getGap = { spacing -> spacing.value200 },
                getTextStyle = { type -> type.bodyBase },
                height = 48.dp,
                iconSize = 24.dp
            )
        ),
        Pair(
            SizeType.Large,
            ButtonSize(
                getPadding = { spacing ->
                    PaddingValues(
                        vertical = spacing.value300,
                        horizontal = spacing.value400
                    )
                },
                getGap = { spacing -> spacing.value300 },
                getTextStyle = { type -> type.bodyLarge },
                height = 56.dp,
                iconSize = 32.dp
            )
        ),
        Pair(
            SizeType.Small,
            ButtonSize(
                getPadding = { spacing ->
                    PaddingValues(
                        vertical = spacing.value150,
                        horizontal = spacing.value200
                    )
                },
                getGap = { spacing -> spacing.value150 },
                getTextStyle = { type -> type.bodySmall },
                height = 40.dp,
                iconSize = 20.dp
            )
        ),
        Pair(
            SizeType.ExtraSmall,
            ButtonSize(
                getPadding = { spacing ->
                    PaddingValues(
                        vertical = spacing.value100,
                        horizontal = spacing.value150
                    )
                },
                getGap = { spacing -> spacing.value100 },
                getTextStyle = { type -> type.bodySmall },
                height = 32.dp,
                iconSize = 16.dp
            )
        )
    )