package com.openlysis.feature.results.components.filter

/**
 * Enum class representing fields that can be used for sorting results.
 *
 * @property Date Sort by date field
 * @property Verdict Sort by verdict field
 * @property Status Sort by status field
 */
internal enum class SortableField {
    Date,
    Verdict,
    Status
}