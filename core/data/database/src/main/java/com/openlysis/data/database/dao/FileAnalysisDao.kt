package com.openlysis.data.database.dao

import androidx.room.Dao
import com.openlysis.data.database.entity.analysis.FileAnalysisEntity

/**
 * Data Access Object (DAO) for [FileAnalysisEntity].
 *
 * Provides database operations for file analysis entities.
 */
@Dao
internal interface FileAnalysisDao : EntityDao<FileAnalysisEntity>