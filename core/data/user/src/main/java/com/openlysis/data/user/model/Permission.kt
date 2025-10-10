package com.openlysis.data.user.model

/**
 * Represents a permission used by the app.
 *
 * @property manifestName The name of the permission as defined in the Android manifest.
 * @property wasGrantedFromApp Indicates if the permission was granted by the user from within the app.
 * @property isGrantedFromSystem Indicates if the permission is currently granted by the system.
 */
data class Permission(
    val manifestName: String,
    val wasGrantedFromApp: Boolean,
    val isGrantedFromSystem: Boolean
)