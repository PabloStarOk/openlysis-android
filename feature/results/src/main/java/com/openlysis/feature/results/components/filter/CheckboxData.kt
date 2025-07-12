package com.openlysis.feature.results.components.filter

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

/**
 * Type used for mapping available checkboxes for filters dialog.
 *
 * @param labelResId A string resource's ID to display as the label of the checkbox.
 * @param data The filterable data this checkbox represents
 */
@Immutable
internal data class CheckboxData<T : Any>(
    @StringRes val labelResId: Int,
    val data: T
)