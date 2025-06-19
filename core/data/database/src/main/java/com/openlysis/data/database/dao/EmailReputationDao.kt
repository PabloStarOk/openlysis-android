package com.openlysis.data.database.dao

import androidx.room.Dao
import com.openlysis.data.database.entity.reputation.EmailReputationEntity

/**
 * Data Access Object (DAO) for [EmailReputationEntity].
 *
 * Provides database operations for email reputation entities.
 */
@Dao
internal interface EmailReputationDao : EntityDao<EmailReputationEntity>