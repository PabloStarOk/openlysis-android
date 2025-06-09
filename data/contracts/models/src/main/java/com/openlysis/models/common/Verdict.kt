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
    Malicious
}