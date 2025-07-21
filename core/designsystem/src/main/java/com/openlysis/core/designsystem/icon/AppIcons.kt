package com.openlysis.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.openlysis.core.designsystem.R

/**
 * Application's icons provided as image vectors.
 */
object AppIcons {
    val Openlysis: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.openlysis_icon)

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

    val RotateLeft
        @Composable get() = ImageVector.vectorResource(R.drawable.rotate_left_icon)

    val GitCommit
        @Composable get() = ImageVector.vectorResource(R.drawable.git_commit_icon)

    val Clock
        @Composable get() = ImageVector.vectorResource(R.drawable.clock_icon)

    val Document
        @Composable get() = ImageVector.vectorResource(R.drawable.document_icon)

    val Refresh
        @Composable get() = ImageVector.vectorResource(R.drawable.refresh_icon)

    val Filter
        @Composable get() = ImageVector.vectorResource(R.drawable.filter_icon)

    val Calendar
        @Composable get() = ImageVector.vectorResource(R.drawable.calendar_icon)

    val Help
        @Composable get() = ImageVector.vectorResource(R.drawable.help_icon)

    val Copy
        @Composable get() = ImageVector.vectorResource(R.drawable.copy_icon)

    val ChevronDown
        @Composable get() = ImageVector.vectorResource(R.drawable.chevron_down_icon)
}