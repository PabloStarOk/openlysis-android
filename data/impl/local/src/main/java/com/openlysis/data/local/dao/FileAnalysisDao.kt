package com.openlysis.data.local.dao

import androidx.room.Dao
import com.openlysis.data.local.entity.analysis.FileAnalysisEntity

/**
 * Data Access Object (DAO) for [FileAnalysisEntity].
 *
 * Provides database operations for file analysis entities.
 */
@Dao
internal interface FileAnalysisDao : EntityDao<FileAnalysisEntity>