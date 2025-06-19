package com.openlysis.data.database.dao

import androidx.room.Dao
import com.openlysis.data.database.entity.reputation.PhoneReputationEntity

/**
 * Data Access Object (DAO) for [PhoneReputationEntity].
 *
 * Provides database operations for phone reputation entities.
 */
@Dao
internal interface PhoneReputationDao : EntityDao<PhoneReputationEntity>