package com.openlysis.feature.auth.model

import androidx.compose.runtime.Immutable

/**
 * Represents a password requirement.
 *
 * @property type The type of password requirement.
 * @property isSatisfied Whether the requirement is currently satisfied.
 */
@Immutable
internal data class PasswordRequirement(
    val type: PasswordRequirementType,
    val isSatisfied: Boolean
)