package com.openlysis.models.common

/**
 * A verdict of an analysis or reputation.
 *
 * This enum defines the possible outcomes when assessing a file, URL,
 * or other data for potential threats.
 */
enum class Verdict {
    Unknown,
    Undetected,
    Suspicious,
    Malicious;

    companion object {
        /**
         * Parses the given string to a corresponding [Verdict] enum value.
         *
         * @param value The string representation of the verdict.
         * @param ignoreCase Whether to ignore case when matching the verdict name.
         * @return The matching [Verdict] enum value.
         * @throws IllegalArgumentException if the input does not match any verdict.
         */
        fun parse(
            value: String,
            ignoreCase: Boolean = true
        ): Verdict =
            Verdict.entries.find {
                it.name.equals(value, ignoreCase)
            } ?: throw IllegalArgumentException("Invalid verdict: $value")
    }
}