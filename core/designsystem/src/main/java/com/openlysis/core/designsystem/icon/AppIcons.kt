package com.openlysis.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.openlysis.core.designsystem.R

/**
 * Application's icons provided as image vectors.
 */
object AppIcons {
    val Search: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.search_icon)

    val Results: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.stats_icon)

    val Settings: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.settings_icon)

    val File: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.file_icon)

    val Link: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.link_icon)

    val Mail: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.mail_icon)

    val Sms: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.sms_icon)

    val OctagonAlert: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.alert_octagon_icon)

    val TriangleAlert: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.alert_triangle_icon)

    val Back: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.arrow_left_icon)

    val Check: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.check_icon)

    val Menu: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.menu_icon)

    val Cross: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.x_icon)

    val Plus: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.plus_icon)

    val Eye: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.eye_icon)

    val EyeOff: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.eye_off_icon)

    val Retry: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.retry_icon)

    val Open: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.open_icon)

    val ArrowRight
        @Composable get() = ImageVector.vectorResource(R.drawable.arrow_right_icon)
}