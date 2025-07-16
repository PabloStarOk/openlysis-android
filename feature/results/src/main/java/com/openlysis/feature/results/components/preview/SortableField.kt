package com.openlysis.feature.results.components.preview

import com.openlysis.feature.results.components.preview.SortableField.Date
import com.openlysis.feature.results.components.preview.SortableField.Status
import com.openlysis.feature.results.components.preview.SortableField.Verdict

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