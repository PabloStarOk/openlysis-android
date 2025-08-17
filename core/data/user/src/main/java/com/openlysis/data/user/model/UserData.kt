package com.openlysis.data.user.model

/**
 * User-related data.
 *
 * @property askedPermissions List of permissions that have been requested from the user.
 * @property themeConfig Configuration for the user's theme.
 */
data class UserData(
    val askedPermissions: List<Permission>,
    val themeConfig: ThemeConfig
)