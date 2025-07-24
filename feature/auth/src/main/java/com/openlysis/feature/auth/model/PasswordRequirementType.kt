package com.openlysis.feature.auth.model

/**
 * Enum representing different password requirements.
 *
 * @property target The target value to satisfy the requirement.
 */
internal enum class PasswordRequirementType(
    val target: Int
) {
    /** Minimum password length requirement. */
    MinLength(target = 8),

    /** Minimum number of lowercase letters required. */
    MinLowerLetters(target = 1),

    /** Minimum number of uppercase letters required. */
    MinUpperLetters(target = 1),

    /** Minimum number of digits required. */
    MinDigits(target = 1),

    /** Minimum number of special characters required. */
    MinSpecialChars(target = 1)
}