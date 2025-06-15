package com.openlysis.data.local.dao

import androidx.room.Dao
import com.openlysis.data.local.entity.reputation.EmailReputationEntity

/**
 * Data Access Object (DAO) for [EmailReputationEntity].
 *
 * Provides database operations for email reputation entities.
 */
@Dao
internal interface EmailReputationDao : EntityDao<EmailReputationEntity>