package com.openlysis.data.local.dao

import androidx.room.Dao
import com.openlysis.data.local.entity.analysis.UrlAnalysisEntity

/**
 * Data Access Object (DAO) for [UrlAnalysisEntity].
 *
 * Provides database operations for URL analysis entities.
 */
@Dao
internal interface UrlAnalysisDao : EntityDao<UrlAnalysisEntity>