package com.openlysis.feature.results.components.preview

import android.text.format.DateFormat
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.openlysis.core.designsystem.components.AppCheckbox
import com.openlysis.core.designsystem.components.button.AppButton
import com.openlysis.core.designsystem.components.button.ButtonType
import com.openlysis.core.designsystem.icon.AppIcons
import com.openlysis.core.designsystem.modifier.SizeType
import com.openlysis.core.designsystem.theme.LocalAppColorScheme
import com.openlysis.core.designsystem.theme.OpenlysisTheme
import com.openlysis.core.designsystem.theme.radius.LocalAppRadius
import com.openlysis.core.designsystem.theme.size.LocalAppSpacing
import com.openlysis.core.designsystem.theme.type.LocalAppTypography
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.feature.results.R
import kotlinx.datetime.TimeZone

/**
 * A dialog to filter the results previews.
 *
 * This dialog allows users to modify various filtering options including sorting fields,
 * verdicts, statuses, and date ranges. It provides options to apply or cancel the changes.
 *
 * @param currentFilters The current state of filters being displayed and modified
 * @param defaultFilters The default state of filters used for resetting
 * @param onApplyRequest Callback invoked when the user confirms filter changes. The parameter represents the new filters requested by the user.
 * @param onDismissRequest Callback invoked when the dialog should be dismissed
 * @param modifier Optional modifier for customizing the dialog's layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FiltersDialog(
    currentFilters: FiltersState,
    defaultFilters: FiltersState,
    onApplyRequest: (FiltersState) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = remember { FiltersDialogState(currentFilters, defaultFilters) }
    val scrollState = rememberScrollState()
    val dateRangePickerState = rememberDateRangePickerState()

    var showDateRangePickerDialog by
        rememberSaveable {
            mutableStateOf(false)
        }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(LocalAppColorScheme.current.background.default.primary)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value600),
                modifier =
                    Modifier
                        .verticalScroll(scrollState)
                        .weight(1f)
                        .padding(LocalAppSpacing.current.value800)
            ) {
                SortBySection(
                    onResetRequest = state::resetSortOrder,
                    onAddRequest = state::addSortingField,
                    onRemoveRequest = state::removeSortingField,
                    selectedFields = state.filters.sortingFields
                )

                HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

                SelectedVerdictsSection(
                    onResetRequest = state::resetSelectedVerdicts,
                    onAddRequest = state::addSelectedVerdict,
                    onRemoveRequest = state::removeSelectedVerdict,
                    selectedVerdicts = state.filters.selectedVerdicts
                )

                HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

                SelectedStatusSection(
                    onResetRequest = state::resetSelectedStatuses,
                    onAddRequest = state::addSelectedStatus,
                    onRemoveRequest = state::removeSelectedStatus,
                    selectedStatuses = state.filters.selectedStatuses
                )

                HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

                DateRangeSection(
                    onResetRequest = state::resetDateRange,
                    onOpenPickerRequest = {
                        dateRangePickerState.setSelection(
                            startDateMillis = state.filters.startDateMillis,
                            endDateMillis = state.filters.endDateMillis
                        )
                        showDateRangePickerDialog = true
                    },
                    startDateMillis = state.filters.startDateMillis,
                    endDateMillis = state.filters.endDateMillis
                )

                HorizontalDivider(color = LocalAppColorScheme.current.border.default.primary)

                AppButton(
                    type = ButtonType.Danger,
                    size = SizeType.Default,
                    onClick = state::resetAll,
                    displayLabel = true,
                    displayIcon = false,
                    label = stringResource(R.string.filters_reset_all_button_label),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
                modifier =
                    Modifier
                        .border(
                            width = 1.dp,
                            color = LocalAppColorScheme.current.border.default.primary
                        ).padding(
                            vertical = LocalAppSpacing.current.value300,
                            horizontal = LocalAppSpacing.current.value600
                        )
            ) {
                AppButton(
                    type = ButtonType.Secondary,
                    size = SizeType.Default,
                    onClick = onDismissRequest,
                    displayLabel = true,
                    displayIcon = false,
                    label = stringResource(R.string.filters_cancel_button_label),
                    modifier = Modifier.weight(1f)
                )

                AppButton(
                    type = ButtonType.Primary,
                    size = SizeType.Default,
                    onClick = { onApplyRequest(state.filters) },
                    displayLabel = true,
                    displayIcon = false,
                    label = stringResource(R.string.filters_apply_button_label),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showDateRangePickerDialog) {
        DateRangePickerDialog(
            onAcceptRequest = {
                state.updateDateRange(
                    startDateMillis = dateRangePickerState.selectedStartDateMillis,
                    endDateMillis = dateRangePickerState.selectedEndDateMillis
                )
                showDateRangePickerDialog = false
            },
            onDismissRequest = { showDateRangePickerDialog = false },
            state = dateRangePickerState
        )
    }
}

@Composable
private fun SortBySection(
    onResetRequest: () -> Unit,
    onAddRequest: (SortableField) -> Unit,
    onRemoveRequest: (SortableField) -> Unit,
    selectedFields: List<SortableField>
) {
    val orderedSelectableFields =
        selectedFields + SortableField.entries.filterNot { it in selectedFields }

    FiltersSection(
        onResetRequest = onResetRequest,
        label = stringResource(R.string.filters_sort_by_section_label)
    ) {
        FlowRow(
            horizontalArrangement =
                Arrangement.spacedBy(
                    LocalAppSpacing.current.value300
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    LocalAppSpacing.current.value300
                )
        ) {
            orderedSelectableFields.forEach {
                var selected by rememberSaveable(selectedFields) {
                    mutableStateOf(it in selectedFields)
                }
                CustomFilterChip(
                    onClick = {
                        selected = !selected
                        if (selected) {
                            onAddRequest(it)
                        } else {
                            onRemoveRequest(it)
                        }
                    },
                    selected = selected,
                    label = stringResource(chipsLabelIdsMap.getValue(it))
                )
            }
        }
    }
}

@Composable
private fun SelectedVerdictsSection(
    onResetRequest: () -> Unit,
    onAddRequest: (Verdict) -> Unit,
    onRemoveRequest: (Verdict) -> Unit,
    selectedVerdicts: List<Verdict>
) {
    FiltersSection(
        onResetRequest = onResetRequest,
        label = stringResource(R.string.filters_verdict_section_label)
    ) {
        verdictCheckboxes.forEach {
            var checked by rememberSaveable(selectedVerdicts) {
                mutableStateOf(
                    it.data in selectedVerdicts
                )
            }
            AppCheckbox(
                onCheckedChange = { targetState ->
                    checked = targetState
                    if (checked) {
                        onAddRequest(it.data)
                    } else {
                        onRemoveRequest(it.data)
                    }
                },
                checked = checked,
                label = stringResource(it.labelResId),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SelectedStatusSection(
    onResetRequest: () -> Unit,
    onAddRequest: (AnalysisStatus) -> Unit,
    onRemoveRequest: (AnalysisStatus) -> Unit,
    selectedStatuses: List<AnalysisStatus>
) {
    FiltersSection(
        onResetRequest = onResetRequest,
        label = stringResource(R.string.filters_status_section_label)
    ) {
        statusCheckboxes.forEach {
            var checked by rememberSaveable(selectedStatuses) {
                mutableStateOf(it.data in selectedStatuses)
            }
            AppCheckbox(
                onCheckedChange = { targetState ->
                    checked = targetState
                    if (checked) {
                        onAddRequest(it.data)
                    } else {
                        onRemoveRequest(it.data)
                    }
                },
                checked = checked,
                label = stringResource(it.labelResId)
            )
        }
    }
}

@Composable
private fun DateRangeSection(
    onResetRequest: () -> Unit,
    onOpenPickerRequest: () -> Unit,
    startDateMillis: Long,
    endDateMillis: Long
) {
    val dateFormat =
        DateFormat.getMediumDateFormat(LocalContext.current).apply {
            timeZone = java.util.TimeZone.getTimeZone(TimeZone.UTC.id)
        }
    val formattedStartDate = dateFormat.format(startDateMillis)
    val formattedEndDate = dateFormat.format(endDateMillis)
    val dateRange = "$formattedStartDate - $formattedEndDate"
    val outputShape = RoundedCornerShape(LocalAppRadius.current.value100)

    FiltersSection(
        onResetRequest = onResetRequest,
        label = stringResource(R.string.filters_date_section_label)
    ) {
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    LocalAppSpacing.current.value300
                )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .background(
                            color = LocalAppColorScheme.current.background.default.primary,
                            shape = outputShape
                        ).border(
                            width = 1.dp,
                            color = LocalAppColorScheme.current.border.default.primary,
                            shape = outputShape
                        ).clip(outputShape)
                        .height(48.dp)
                        .weight(1f)
            ) {
                Text(
                    text = dateRange,
                    style = LocalAppTypography.current.bodyBase,
                    color = LocalAppColorScheme.current.text.default.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier =
                        Modifier
                            .padding(
                                vertical = LocalAppSpacing.current.value200,
                                horizontal = LocalAppSpacing.current.value300
                            ).fillMaxWidth()
                )
            }

            AppButton(
                type = ButtonType.Secondary,
                size = SizeType.Default,
                onClick = onOpenPickerRequest,
                displayLabel = false,
                displayIcon = true,
                icon = AppIcons.Calendar,
                iconAlt = stringResource(R.string.filters_date_select_button_icon_alt)
            )
        }
    }
}

@Composable
private fun FiltersSection(
    onResetRequest: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value300),
        modifier = modifier
    ) {
        Text(
            text = label,
            style = LocalAppTypography.current.title6,
            color = LocalAppColorScheme.current.text.brand.primary,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        content()

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppButton(
                type = ButtonType.Tertiary,
                size = SizeType.Small,
                onClick = onResetRequest,
                displayLabel = true,
                displayIcon = false,
                label = stringResource(R.string.filters_section_reset_button_label)
            )
        }
    }
}

@Composable
private fun CustomFilterChip(
    onClick: () -> Unit,
    selected: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    val borderColor =
        if (selected) {
            LocalAppColorScheme.current.border.brand.tertiary
        } else {
            LocalAppColorScheme.current.border.default.primary
        }

    val labelColor =
        if (selected) {
            LocalAppColorScheme.current.text.brand.onTertiary
        } else {
            LocalAppColorScheme.current.text.default.secondary
        }

    FilterChip(
        onClick = onClick,
        selected = selected,
        label = {
            Text(
                text = label,
                style = LocalAppTypography.current.bodySmall,
                color = labelColor
            )
        },
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = AppIcons.Check,
                    contentDescription = stringResource(R.string.filters_chip_checkmark_icon_alt),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors =
            FilterChipDefaults.filterChipColors().copy(
                containerColor = LocalAppColorScheme.current.background.default.secondary,
                labelColor = LocalAppColorScheme.current.text.default.secondary,
                selectedContainerColor = LocalAppColorScheme.current.background.brand.tertiary,
                selectedLabelColor = LocalAppColorScheme.current.text.brand.onTertiary,
                selectedLeadingIconColor = LocalAppColorScheme.current.icon.brand.onTertiary
            ),
        border =
            BorderStroke(
                width = 1.dp,
                color = borderColor
            ),
        shape = RoundedCornerShape(LocalAppRadius.current.value100),
        modifier = modifier.animateContentSize(animationSpec = tween(200))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    onAcceptRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    state: DateRangePickerState,
    modifier: Modifier = Modifier
) {
    val dateFormat =
        DateFormat
            .getMediumDateFormat(LocalContext.current)
            .apply {
                timeZone = java.util.TimeZone.getTimeZone(TimeZone.UTC.id)
            }
    val formattedStartDate =
        if (state.selectedStartDateMillis != null) {
            dateFormat.format(state.selectedStartDateMillis)
        } else {
            stringResource(R.string.filters_date_dialog_min_date_label)
        }
    val formattedEndDate =
        if (state.selectedEndDateMillis != null) {
            dateFormat.format(state.selectedEndDateMillis)
        } else {
            stringResource(R.string.filters_date_dialog_max_date_label)
        }

    val shape = RoundedCornerShape(LocalAppSpacing.current.value100)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalAppSpacing.current.value200),
            modifier =
                modifier
                    .fillMaxSize()
                    .clip(shape = shape)
                    .background(
                        color = LocalAppColorScheme.current.background.default.primary,
                        shape = shape
                    ).padding(LocalAppSpacing.current.value200)
        ) {
            DateRangePicker(
                state = state,
                showModeToggle = false,
                colors =
                    DatePickerColors(
                        containerColor = LocalAppColorScheme.current.background.default.primary,
                        titleContentColor = LocalAppColorScheme.current.text.default.secondary,
                        headlineContentColor = LocalAppColorScheme.current.text.default.primary,
                        weekdayContentColor = LocalAppColorScheme.current.text.default.primary,
                        subheadContentColor = LocalAppColorScheme.current.text.default.primary,
                        navigationContentColor = LocalAppColorScheme.current.icon.brand.primary,
                        yearContentColor = LocalAppColorScheme.current.text.default.primary,
                        dayContentColor = LocalAppColorScheme.current.text.default.primary,
                        currentYearContentColor = LocalAppColorScheme.current.text.default.primary,
                        selectedDayContainerColor =
                            LocalAppColorScheme.current.background.brand.primary,
                        selectedDayContentColor = LocalAppColorScheme.current.text.brand.onPrimary,
                        selectedYearContainerColor =
                            LocalAppColorScheme.current.background.brand.primary,
                        selectedYearContentColor = LocalAppColorScheme.current.text.brand.onPrimary,
                        todayContentColor = LocalAppColorScheme.current.text.brand.primary,
                        todayDateBorderColor = LocalAppColorScheme.current.border.brand.primary,
                        disabledYearContentColor =
                            LocalAppColorScheme.current.text.disabled.primary,
                        disabledDayContentColor = LocalAppColorScheme.current.text.disabled.primary,
                        disabledSelectedDayContainerColor =
                            LocalAppColorScheme.current.background.disabled.primary,
                        disabledSelectedYearContainerColor =
                            LocalAppColorScheme.current.background.disabled.primary,
                        disabledSelectedYearContentColor =
                            LocalAppColorScheme.current.text.disabled.onPrimary,
                        disabledSelectedDayContentColor =
                            LocalAppColorScheme.current.text.disabled.onPrimary,
                        dayInSelectionRangeContainerColor =
                            LocalAppColorScheme.current.background.brand.tertiary,
                        dayInSelectionRangeContentColor =
                            LocalAppColorScheme.current.text.brand.onTertiary,
                        dividerColor = LocalAppColorScheme.current.border.default.primary,
                        dateTextFieldColors = TextFieldDefaults.colors()
                    ),
                title = null,
                headline = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                space = LocalAppSpacing.current.value200,
                                alignment = Alignment.CenterHorizontally
                            ),
                        modifier =
                            Modifier
                                .height(96.dp)
                                .padding(
                                    vertical = LocalAppSpacing.current.value400,
                                    horizontal = LocalAppSpacing.current.value200
                                )
                    ) {
                        Text(
                            text = formattedStartDate,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "-",
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = formattedEndDate,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        space = LocalAppSpacing.current.value300,
                        alignment = Alignment.End
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
            ) {
                AppButton(
                    type = ButtonType.Secondary,
                    size = SizeType.Default,
                    onClick = onDismissRequest,
                    displayLabel = true,
                    displayIcon = true,
                    label = stringResource(R.string.filters_date_dialog_cancel_button_label),
                    icon = AppIcons.Cross,
                    iconAlt = stringResource(R.string.filters_date_dialog_cancel_button_icon_alt)
                )

                AppButton(
                    type = ButtonType.Primary,
                    size = SizeType.Default,
                    onClick = onAcceptRequest,
                    displayLabel = true,
                    displayIcon = true,
                    label = stringResource(R.string.filters_date_dialog_accept_button_label),
                    icon = AppIcons.Check,
                    iconAlt = stringResource(R.string.filters_date_dialog_accept_button_icon_alt)
                )
            }
        }
    }
}

private val chipsLabelIdsMap =
    mapOf(
        Pair(
            SortableField.Date,
            R.string.filters_sort_by_date_label
        ),
        Pair(
            SortableField.Verdict,
            R.string.filters_sort_by_verdict_label
        ),
        Pair(
            SortableField.Status,
            R.string.filters_sort_by_status_label
        )
    )

private val verdictCheckboxes =
    listOf(
        CheckboxData(
            labelResId = R.string.analysis_verdict_malicious_badge,
            data = Verdict.Malicious
        ),
        CheckboxData(
            labelResId = R.string.analysis_verdict_suspicious_badge,
            data = Verdict.Suspicious
        ),
        CheckboxData(
            labelResId = R.string.analysis_verdict_clean_badge,
            data = Verdict.Undetected
        ),
        CheckboxData(
            labelResId = R.string.analysis_verdict_unknown_badge,
            data = Verdict.Unknown
        )
    )

private val statusCheckboxes =
    listOf(
        CheckboxData(
            labelResId = R.string.filters_status_completed_label,
            data = AnalysisStatus.Completed
        ),
        CheckboxData(
            labelResId = R.string.filters_status_in_progress_label,
            data = AnalysisStatus.InProgress
        ),
        CheckboxData(
            labelResId = R.string.filters_status_queued_label,
            data = AnalysisStatus.Queued
        ),
        CheckboxData(
            labelResId = R.string.filters_status_failed_label,
            data = AnalysisStatus.Failed
        ),
        CheckboxData(
            labelResId = R.string.filters_status_timeout_label,
            data = AnalysisStatus.Timeout
        )
    )

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun FiltersDialogPreview() {
    OpenlysisTheme {
        FiltersDialog(
            currentFilters = FiltersState.Default,
            defaultFilters = FiltersState.Default,
            onApplyRequest = { },
            onDismissRequest = { }
        )
    }
}