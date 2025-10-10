package com.openlysis.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.openlysis.R
import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.feature.results.navigation.ResultsBaseRoute
import com.openlysis.feature.results.navigation.ResultsRoute
import com.openlysis.feature.settings.navigation.SettingsRoute
import com.openlysis.feature.tools.navigation.ToolsBaseRoute
import com.openlysis.feature.tools.navigation.ToolsRoute
import kotlin.reflect.KClass

/**
 * Represents the top-level destinations in the application's navigation.
 *
 * @property iconResId The resource ID of the icon for the destination.
 * @property iconAltResId The resource ID of the alternative text for the icon (for accessibility).
 * @property navBarItemLabelResId The resource ID of the label for the navigation bar item.
 * @property route The [KClass] representing the destination's route.
 * @property baseRoute The base route class for nested navigation. Defaults to [route] if not specified.
 */
internal enum class TopLevelDestination(
    @DrawableRes val iconResId: Int,
    @StringRes val iconAltResId: Int,
    @StringRes val navBarItemLabelResId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route
) {
    Tools(
        iconResId = AppIconsIds.Search,
        iconAltResId = R.string.nav_bar_tools_icon_alt,
        navBarItemLabelResId = R.string.nav_bar_tools_label,
        route = ToolsRoute::class,
        baseRoute = ToolsBaseRoute::class
    ),
    Results(
        iconResId = AppIconsIds.Results,
        iconAltResId = R.string.nav_bar_results_icon_alt,
        navBarItemLabelResId = R.string.nav_bar_results_label,
        route = ResultsRoute::class,
        baseRoute = ResultsBaseRoute::class
    ),
    Settings(
        iconResId = AppIconsIds.Settings,
        iconAltResId = R.string.nav_bar_settings_icon_alt,
        navBarItemLabelResId = R.string.nav_bar_settings_label,
        route = SettingsRoute::class
    )
}