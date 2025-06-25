package com.openlysis.feature.tools.data

import com.openlysis.feature.tools.data.ToolCategory.Email
import com.openlysis.feature.tools.data.ToolCategory.File
import com.openlysis.feature.tools.data.ToolCategory.None
import com.openlysis.feature.tools.data.ToolCategory.Sms
import com.openlysis.feature.tools.data.ToolCategory.Url

/**
 * Represents the categories available for tools.
 *
 * @property None No category is shown.
 * @property Email Category for email analysis.
 * @property Sms Category for SMS analysis.
 * @property Url Category for URL analysis.
 * @property File Category for file analysis.
 */
internal enum class ToolCategory {
    /** No category is shown. */
    None,

    /** Category for email analysis. */
    Email,

    /** Category for SMS analysis. */
    Sms,

    /** Category for URL analysis. */
    Url,

    /** Category for file analysis. */
    File
}