package com.openlysis.feature.tools.data

import com.openlysis.feature.tools.R

/**
 * A data source implementation for retrieving a list of available tools.
 * This class implements the [ToolsRepository] interface.
 */
internal class ToolsDataSource : ToolsRepository {
    private val messageAnalysisTools =
        listOf(
            Tool(
                nameResource = R.string.email_message_tool_name,
                descriptionResource = R.string.email_message_tool_description,
                iconResource = R.drawable.mail_icon,
                iconAltResource = R.string.email_messages_tool_icon_alt,
                onClick = { }
            ),
            Tool(
                nameResource = R.string.sms_message_tool_name,
                descriptionResource = R.string.sms_message_tool_description,
                iconResource = R.drawable.sms_icon,
                iconAltResource = R.string.sms_messages_tool_icon_alt,
                onClick = { }
            )
        )

    private val fileAndUrlTools =
        listOf(
            Tool(
                nameResource = R.string.file_tool_name,
                descriptionResource = R.string.file_tool_description,
                iconResource = R.drawable.file_icon,
                iconAltResource = R.string.file_tool_icon_alt,
                onClick = { }
            ),
            Tool(
                nameResource = R.string.url_tool_name,
                descriptionResource = R.string.url_tool_description,
                iconResource = R.drawable.link_icon,
                iconAltResource = R.string.url_tool_icon_alt,
                onClick = { }
            )
        )

    private val reputationTools =
        listOf(
            Tool(
                nameResource = R.string.email_address_tool_name,
                descriptionResource = R.string.email_address_tool_description,
                iconResource = R.drawable.atsign_icon,
                iconAltResource = R.string.email_address_tool_icon_alt,
                onClick = { }
            ),
            Tool(
                nameResource = R.string.phone_number_tool_name,
                descriptionResource = R.string.phone_number_tool_description,
                iconResource = R.drawable.phone_icon,
                iconAltResource = R.string.phone_number_tool_icon_alt,
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

    /**
     * Retrieves a list of reputation-related tools.
     *
     * This function provides access to a predefined list of `Tool` objects
     * that are specifically designed or related to managing or interacting
     * with reputation within the application.
     *
     * @return A `List` of `Tool` objects representing the reputation tools.
     */
    override fun getReputationTools(): List<Tool> = reputationTools
}