package com.openlysis.feature.auth.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

/**
 * Holds string resource IDs for form UI elements of the auth screen.
 *
 * @property title Resource ID for the form title.
 * @property submitLabel Resource ID for the submit button label.
 * @property switchTypeLabel Resource ID for the label describing the switch type action.
 * @property switchTypeButtonLabel Resource ID for the switch type button label.
 */
@Immutable
internal data class FormResourcesIds(
    @StringRes val title: Int,
    @StringRes val submitLabel: Int,
    @StringRes val switchTypeLabel: Int,
    @StringRes val switchTypeButtonLabel: Int
)