package com.openlysis.data.auth

/**
 * Data required for user's authentication with the back-end API.
 *
 * @property isSignedIn Indicates if the user is signed in.
 * @property apiKey The API key associated with the user, if [isSignedIn] is true.
 */
data class UserAuthData(
    val isSignedIn: Boolean,
    val apiKey: String?
)