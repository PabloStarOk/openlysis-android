package com.openlysis.feature.results.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.openlysis.data.analysis.model.common.Model
import com.openlysis.feature.results.PreviewsScreenViewModel

/**
 * Data for the Previews screen.
 *
 * @param TModel The type of model to display in the previews.
 * @property getViewModel Composable function that provides an implementation of [PreviewsScreenViewModel] for the previews screen.
 * @property screenTitleResId Resource ID for the screen title.
 * @property previewsHeaderLabelResId Resource ID for the previews' header label.
 */
internal data class PreviewsScreenData<TModel : Model>(
    val getViewModel: @Composable () -> PreviewsScreenViewModel<TModel>,
    @StringRes val screenTitleResId: Int,
    @StringRes val previewsHeaderLabelResId: Int
)