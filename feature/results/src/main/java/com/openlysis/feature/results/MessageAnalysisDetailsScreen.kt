package com.openlysis.feature.results

import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openlysis.core.designsystem.components.bar.TopBarState
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.HashValues
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.MessageAnalysis
import com.openlysis.data.analysis.model.reputation.MultiReputation
import com.openlysis.feature.results.components.AnalysisStatusBadge
import com.openlysis.feature.results.components.AnalysisVerdictBadge
import com.openlysis.feature.results.components.detail.DetailsScreenScaffold
import com.openlysis.feature.results.components.detail.DetailsScreenScaffoldData
import com.openlysis.feature.results.components.detail.HashValuesSection
import com.openlysis.feature.results.components.detail.InformationCard
import com.openlysis.feature.results.components.detail.SectionAccordion
import com.openlysis.feature.results.components.detail.ServiceResultData
import com.openlysis.feature.results.components.detail.ServiceResultsSection

/**
 * Details screen for a message analysis.
 *
 * @param viewModel The ViewModel providing the UI state for the message analysis.
 * @param onTopBarUpdate Callback to update the top bar state.
 * @param screenTitle The title to display on the screen.
 * @param analysisId The ID of the analysis to load and display.
 * @param modifier Modifier for styling the composable.
 */
@Composable
internal fun MessageAnalysisDetailsScreen(
    viewModel: DetailsScreenViewModel<MessageAnalysis>,
    onTopBarUpdate: (TopBarState) -> Unit,
    screenTitle: String,
    analysisId: String,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPolling by viewModel.isPolling.collectAsStateWithLifecycle()
    val analysis =
        if (uiState is DetailsUiState.Success) {
            val success = uiState as DetailsUiState.Success<MessageAnalysis>
            success.analysis
        } else {
            null
        }

    val scaffoldData =
        analysis?.let {
            DetailsScreenScaffoldData(
                heroInfoCardData = null,
                status = analysis.status,
                verdict = analysis.verdict,
                startedDate = analysis.startedDate,
                informationSectionTitle =
                    stringResource(
                        R.string.details_screen_message_information_section_title
                    ),
                informationSectionItems =
                    buildList {
                        add(
                            Pair(
                                stringResource(R.string.details_screen_message_sender_label),
                                analysis.message.sender
                            )
                        )
                        analysis.message.subject?.let {
                            add(
                                Pair(
                                    stringResource(R.string.details_screen_message_subject_label),
                                    analysis.message.subject as String
                                )
                            )
                        }
                        add(
                            Pair(
                                stringResource(R.string.details_screen_message_content_label),
                                analysis.message.content
                            )
                        )
                    }
            )
        }

    DetailsScreenScaffold(
        onTopBarUpdate = onTopBarUpdate,
        onLoadDetails = { viewModel.loadAnalysis(analysisId) },
        onPollingStart = { viewModel.startPolling(analysisId) },
        onPollingStop = viewModel::stopPolling,
        screenTitle = screenTitle,
        uiState = uiState,
        isPolling = isPolling,
        data = scaffoldData,
        modifier = modifier
    ) {
        if (analysis == null) {
            return@DetailsScreenScaffold
        }

        if (analysis.fileMultiAnalyses.isEmpty() &&
            analysis.urlMultiAnalyses.isEmpty() &&
            analysis.emailAddressMultiReputations.isEmpty() &&
            analysis.phoneNumberMultiReputations.isEmpty()
        ) {
            Text(
                text = stringResource(R.string.details_screen_message_no_analyzable_data),
                style = LocalAppTypography.current.bodyBase,
                color = LocalAppColorScheme.current.text.default.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            return@DetailsScreenScaffold
        }

        if (analysis.fileMultiAnalyses.isNotEmpty()) {
            SubResultsSection(
                sectionTitle =
                    stringResource(
                        R.string.details_screen_message_analyzed_files_section_title
                    ),
                subResultHeaderLabel =
                    stringResource(
                        R.string.details_screen_message_analyzed_file_label
                    ),
                initiallyExpanded = true,
                subResults =
                    analysis.fileMultiAnalyses.map {
                        val formattedFileSize =
                            Formatter.formatFileSize(
                                LocalContext.current,
                                it.fileMetadata.size
                            )
                        SubResultData(
                            headerValue = it.fileMetadata.name,
                            status = it.status,
                            verdict = it.finalVerdict,
                            informationSectionItems =
                                listOf(
                                    Pair(
                                        stringResource(
                                            R.string.details_screen_file_information_type_label
                                        ),
                                        it.fileMetadata.contentType
                                    ),
                                    Pair(
                                        stringResource(
                                            R.string.details_screen_file_information_size_label
                                        ),
                                        formattedFileSize
                                    )
                                ),
                            serviceResults =
                                it.analyses.map { analysis ->
                                    ServiceResultData(
                                        serviceName = analysis.serviceName,
                                        status = analysis.status,
                                        verdict = analysis.verdict,
                                        threatScore = analysis.threatScore
                                    )
                                },
                            hashValues = it.hashValues
                        )
                    }
            )
        }

        if (analysis.urlMultiAnalyses.isNotEmpty()) {
            SubResultsSection(
                sectionTitle =
                    stringResource(
                        R.string.details_screen_message_analyzed_urls_section_title
                    ),
                subResultHeaderLabel =
                    stringResource(
                        R.string.details_screen_message_analyzed_url_label
                    ),
                initiallyExpanded = true,
                subResults =
                    analysis.urlMultiAnalyses.map {
                        SubResultData(
                            headerValue = it.url.toString(),
                            status = it.status,
                            verdict = it.finalVerdict,
                            informationSectionItems = null,
                            serviceResults =
                                it.analyses.map { analysis ->
                                    ServiceResultData(
                                        serviceName = analysis.serviceName,
                                        status = analysis.status,
                                        verdict = analysis.verdict,
                                        threatScore = analysis.threatScore
                                    )
                                },
                            hashValues = it.hashValues
                        )
                    }
            )
        }

        if (analysis.emailAddressMultiReputations.isNotEmpty()) {
            MultiReputationSection(
                sectionTitle =
                    stringResource(
                        R.string.details_screen_message_reputation_email_section_title
                    ),
                subResultHeaderLabel =
                    stringResource(
                        R.string.details_screen_message_reputation_email_label
                    ),
                multiReputations = analysis.emailAddressMultiReputations
            )
        }

        if (analysis.phoneNumberMultiReputations.isNotEmpty()) {
            MultiReputationSection(
                sectionTitle =
                    stringResource(
                        R.string.details_screen_message_reputation_phone_section_title
                    ),
                subResultHeaderLabel =
                    stringResource(
                        R.string.details_screen_message_reputation_phone_label
                    ),
                multiReputations = analysis.phoneNumberMultiReputations
            )
        }
    }
}

@Composable
private fun MultiReputationSection(
    sectionTitle: String,
    subResultHeaderLabel: String,
    multiReputations: List<MultiReputation<*>>
) {
    SubResultsSection(
        sectionTitle = sectionTitle,
        subResultHeaderLabel = subResultHeaderLabel,
        initiallyExpanded = true,
        subResults =
            multiReputations.map {
                SubResultData(
                    headerValue = it.data,
                    status = null,
                    verdict = it.finalVerdict,
                    informationSectionItems = null,
                    serviceResults =
                        it.reputations.map { reputation ->
                            ServiceResultData(
                                serviceName = reputation.serviceName,
                                status = null,
                                verdict = reputation.verdict,
                                threatScore = null
                            )
                        },
                    hashValues = null
                )
            }
    )
}

@Composable
private fun SubResultsSection(
    sectionTitle: String,
    subResultHeaderLabel: String,
    subResults: List<SubResultData>,
    initiallyExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    SectionAccordion(
        title = sectionTitle,
        initiallyExpanded = initiallyExpanded,
        modifier = modifier
    ) {
        subResults.forEach { subResult ->
            SubResultAccordion(
                headerLabel = subResultHeaderLabel,
                headerValue = subResult.headerValue
            ) {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            LocalAppSpacing.current.value200
                        ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (subResult.status != null) {
                        AnalysisStatusBadge(subResult.status)
                    }

                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(
                                LocalAppSpacing.current.value050
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = LocalAppColorScheme.current.border.default.primary,
                                    shape =
                                        RoundedCornerShape(
                                            LocalAppRadius.current.value100
                                        )
                                ).padding(LocalAppSpacing.current.value300)
                    ) {
                        Text(
                            text =
                                stringResource(
                                    R.string.details_screen_message_sub_result_final_verdict_label
                                ),
                            style = LocalAppTypography.current.bodySmall,
                            color = LocalAppColorScheme.current.text.default.secondary
                        )

                        AnalysisVerdictBadge(
                            verdict = subResult.verdict,
                            size = SizeType.Small,
                            modifier = Modifier.width(IntrinsicSize.Min)
                        )
                    }
                }

                if (subResult.informationSectionItems != null) {
                    SectionAccordion(
                        title =
                            stringResource(
                                R.string.details_screen_message_sub_result_information_section_title
                            ),
                        isPrimarySection = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        subResult.informationSectionItems.forEach { pair ->
                            InformationCard(
                                label = pair.first,
                                information = pair.second
                            )
                        }
                    }
                }

                ServiceResultsSection(
                    isPrimarySection = false,
                    serviceResults = subResult.serviceResults
                )

                if (subResult.hashValues != null) {
                    HashValuesSection(
                        hashValues = subResult.hashValues,
                        isPrimarySection = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SubResultAccordion(
    headerLabel: String,
    headerValue: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val iconDegrees by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "details_sub_result_accordion_animation"
    )
    val headerShape = RoundedCornerShape(LocalAppRadius.current.value100)

    Column(
        modifier =
            modifier
                .clip(headerShape)
                .background(
                    color = LocalAppColorScheme.current.background.default.primary,
                    shape = headerShape
                ).border(
                    width = 1.dp,
                    color = LocalAppColorScheme.current.border.default.primary,
                    shape = headerShape
                )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = expanded,
                        onValueChange = { expanded = it },
                        role = Role.Button
                    ).padding(LocalAppSpacing.current.value300)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = headerLabel,
                    style = LocalAppTypography.current.bodySmall,
                    color = LocalAppColorScheme.current.text.default.secondary
                )

                Text(
                    text = headerValue,
                    style = LocalAppTypography.current.bodyBaseStrong,
                    color = LocalAppColorScheme.current.text.default.primary
                )
            }

            Icon(
                imageVector = AppIcons.ChevronDown,
                contentDescription =
                    stringResource(
                        if (expanded) {
                            R.string.details_section_accordion_expanded_chevron_icon_alt
                        } else {
                            R.string.details_section_accordion_collapsed_chevron_icon_alt
                        }
                    ),
                tint = LocalAppColorScheme.current.icon.default.primary,
                modifier =
                    Modifier
                        .size(24.dp)
                        .rotate(iconDegrees)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value600),
                modifier = Modifier.padding(LocalAppSpacing.current.value300)
            ) {
                content()
            }
        }
    }
}

@Immutable
private data class SubResultData(
    val headerValue: String,
    val status: AnalysisStatus?,
    val verdict: Verdict,
    val informationSectionItems: List<Pair<String, String>>?,
    val serviceResults: List<ServiceResultData>,
    val hashValues: HashValues?
)