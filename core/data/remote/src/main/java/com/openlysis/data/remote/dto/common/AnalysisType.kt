package com.openlysis.data.remote.dto.common

import com.openlysis.data.remote.dto.common.AnalysisType.Email
import com.openlysis.data.remote.dto.common.AnalysisType.File
import com.openlysis.data.remote.dto.common.AnalysisType.Sms
import com.openlysis.data.remote.dto.common.AnalysisType.Url

/**
 * Type of an analysis.
 *
 * @property Url Analysis of a URL.
 * @property File Analysis of file.
 * @property Email Analysis of an email message.
 * @property Sms Analysis of an SMS message.
 */
internal enum class AnalysisType {
    Url,
    File,
    Email,
    Sms
}