package com.openlysis.feature.tools

import androidx.lifecycle.ViewModel
import com.openlysis.feature.tools.data.ToolsDataSource
import com.openlysis.feature.tools.data.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for tools initial screen.
 */
@HiltViewModel
internal class ToolsScreenViewModel
    @Inject
    constructor() : ViewModel() {
        val toolsRepository: ToolsRepository = ToolsDataSource()
    }