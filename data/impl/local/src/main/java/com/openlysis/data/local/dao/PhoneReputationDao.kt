package com.openlysis.data.local.dao

import androidx.room.Dao
import com.openlysis.data.local.entity.reputation.PhoneReputationEntity

/**
 * Data Access Object (DAO) for [PhoneReputationEntity].
 *
 * Provides database operations for phone reputation entities.
 */
@Dao
internal interface PhoneReputationDao : EntityDao<PhoneReputationEntity>