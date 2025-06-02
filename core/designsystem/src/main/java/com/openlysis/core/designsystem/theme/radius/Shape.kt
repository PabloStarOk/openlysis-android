package com.openlysis.core.designsystem.theme.radius

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

internal val Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(Radius.Value100),
        small = RoundedCornerShape(Radius.Value100),
        medium = RoundedCornerShape(Radius.Value100),
        large = RoundedCornerShape(Radius.Value200),
        extraLarge = RoundedCornerShape(Radius.Value400)
    )