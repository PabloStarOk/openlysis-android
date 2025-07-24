package com.openlysis.navigation

import com.openlysis.feature.auth.navigation.AuthBaseRoute
import com.openlysis.feature.tools.navigation.ToolsBaseRoute
import kotlin.reflect.KClass

/**
 * Represents the root destinations in the application's navigation.
 *
 * @property startRoute The starting route class for each root destination.
 * @property startBaseRoute The base route object for each root destination.
 */
internal enum class RootDestination(
    val startRoute: KClass<*>,
    val startBaseRoute: Any
) {
    /**
     * Top-level destination.
     *
     * @param startRoute [TopLevelDestination.Tools]
     * @param startBaseRoute [ToolsBaseRoute]
     */
    TopLevel(
        startRoute = TopLevelDestination.Tools.route,
        startBaseRoute = ToolsBaseRoute
    ),

    /**
     * Authentication destination.
     *
     * @param startRoute [AuthDestination.Welcome]
     * @param startBaseRoute [AuthBaseRoute]
     */
    Authentication(
        startRoute = AuthDestination.Welcome.route,
        startBaseRoute = AuthBaseRoute
    )
}