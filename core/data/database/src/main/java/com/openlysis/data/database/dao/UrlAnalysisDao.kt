package com.openlysis.data.database.dao

import androidx.room.Dao
import com.openlysis.data.database.entity.analysis.UrlAnalysisEntity

/**
 * Data Access Object (DAO) for [UrlAnalysisEntity].
 *
 * Provides database operations for URL analysis entities.
 */
@Dao
internal interface UrlAnalysisDao : EntityDao<UrlAnalysisEntity>