package com.openlysis.feature.tools.data

import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.feature.tools.R

/**
 * A data source implementation for retrieving a list of available analysis tools.
 */
internal class ToolsDataSource : ToolsRepository {
    private val messageAnalysisTools =
        listOf(
            Tool(
                nameResource = R.string.email_message_tool_name,
                descriptionResource = R.string.email_message_tool_description,
                iconResource = AppIconsIds.Mail,
                iconAltResource = R.string.email_messages_tool_icon_alt,
                onClick = { }
            ),
            Tool(
                nameResource = R.string.sms_message_tool_name,
                descriptionResource = R.string.sms_message_tool_description,
                iconResource = AppIconsIds.Sms,
                iconAltResource = R.string.sms_messages_tool_icon_alt,
                onClick = { }
            )
        )

    private val fileAndUrlTools =
        listOf(
            Tool(
                nameResource = R.string.file_tool_name,
                descriptionResource = R.string.file_tool_description,
                iconResource = AppIconsIds.File,
                iconAltResource = R.string.file_tool_icon_alt,
                onClick = { }
            ),
            Tool(
                nameResource = R.string.url_tool_name,
                descriptionResource = R.string.url_tool_description,
                iconResource = AppIconsIds.Link,
                iconAltResource = R.string.url_tool_icon_alt,
                onClick = { }
            )
        )

    /**
     * Returns a list of tools available for message analysis.
     *
     * This function overrides a method from a parent class (presumably an interface
     * or base class related to messaging or analysis). It provides access to a
     * predefined list of tools that can be used to analyze messages.
     *
     * @return A [List] of [Tool] objects. Each [Tool] represents a capability
     *   or service that can perform some form of message analysis.
     */
    override fun getMessageAnalysisTools(): List<Tool> = messageAnalysisTools

    /**
     * Returns a list of [Tool] objects that are used for data analysis.
     *
     * These tools are typically capable of processing and analyzing data
     * from files or URLs.
     *
     * @return A list of [Tool] objects for data analysis.
     */
    override fun getDataAnalysisTools(): List<Tool> = fileAndUrlTools
}